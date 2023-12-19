package com.example.UserManagement.User.Service;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.UserManagement.User.Entity.Users;
import com.example.UserManagement.User.Generics.GenericResult;
import org.springframework.stereotype.Service;

 @Service
    public class UserServiceImpl implements UserService {

	@Autowired
    private  MongoTemplate mongoTemplate;
	public UserServiceImpl(MongoTemplate mongoTemplate ) {
    this.mongoTemplate = mongoTemplate;
	}
    
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
	}

	  @Override
      public GenericResult<Page<Users>> getUsers(Map<String, Object> users, int page, int size, String sortField, String sortOrder) {
        GenericResult<Page<Users>> result = new GenericResult<>();
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Users> userPage;
        long startTime = System.currentTimeMillis();
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();

            if (users != null && !users.isEmpty()) {
                if (users.containsKey("id")) {
                    Integer id = Integer.parseInt(users.get("id").toString());
                    criteria.and("id").is(id);
                } else {
                    Map.Entry<String, Object> entry = users.entrySet().iterator().next();
                    criteria = createCriteriaForFilter(entry.getKey(), entry.getValue());
                }
            }
            query.addCriteria(criteria);
            query.with(pageable);
            List<Users> usersList = mongoTemplate.find(query, Users.class);
            long totalCount = mongoTemplate.count(query, Users.class);
            userPage = new PageImpl<>(usersList, pageable, totalCount);
            result.setSuccess(true);
            result.setMessage("Users found");
            result.setData(userPage);
        } catch (Exception e) {
			handleException(result, e, "Error in getUsers method");
            result.setSuccess(false);
            result.setMessage("Users not found " + e.getMessage());
            logger.error("Users not found", e);
        }
        long endTime = System.currentTimeMillis();
        logger.info("GET API Response Time: {} ms", endTime - startTime);
        return result;
    }
	private Criteria createCriteriaForFilter(String key, Object value) {
		Criteria criteria = new Criteria();	
		String[] fieldsToMatch = {
			"firstName", "secondName", "password", "userPhone", "userEmail",
			"customerId", "customerNumber", "userRole", "jobPosition", "country",
			"street", "city", "state", "zip"
		};	
		for (String field : fieldsToMatch) {
			if (field.equals(key)) {
				criteria.and(field).regex(value.toString());
				break;  
			}
		}
		return criteria;
	}
    @Override
public GenericResult<Users> createUser(List<Users> newUsers) {
    GenericResult<Users> genericResult = new GenericResult<>();
    long startTime = System.currentTimeMillis();
    try {
         for (Users users : newUsers) {
             validateUser(users);

			 users.setUpdateDate(LocalDateTime.now());
			 users.setCreateDate(LocalDateTime.now());
			 String createdBy = getCurrentAuditor().get();
			 users.setCreatedBy(createdBy);
            users.setId(SequenceGeneratorService.getSeqNumber(users.getSEQUENCE_NAME()));
            mongoTemplate.save(users);
        }
        genericResult.setSuccess(true);
        genericResult.setMessage("users Created");
        logger.info("users Saved");
        return genericResult;
    } catch (Exception e) {
        handleException(genericResult, e, "Error in createusers method");
        genericResult.setSuccess(false);
        genericResult.setMessage("users failed to create: " + e.getMessage());
        logger.error("users failed to create", e);
    } finally {
        long endTime = System.currentTimeMillis();
        logger.info("DB Create Execution Time: {} ms", endTime - startTime);
    }
    return genericResult;
}
 
private void validateUser(Users tenant) {
    List<String> requiredFields = Arrays.asList(

	 "firstName", "secondName", "password", "userPhone", "userEmail",
           "customerId", "customerNumber", "userRole", "jobPosition", "country",
             "street", "city", "state", "zip"
            
    );

    List<String> missingFields = new ArrayList<>();
 
    for (String field : requiredFields) {
        try {
            Field declaredField = Users.class.getDeclaredField(field);
            declaredField.setAccessible(true);
            Object value = declaredField.get(tenant);
 
            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                missingFields.add(field);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
 
    if (!missingFields.isEmpty()) {
        throw new IllegalArgumentException("Missing required fields: " + String.join(", ", missingFields));
    }
    }
@Override
public GenericResult<String> updateUser(List<Users> updates) {
    GenericResult<String> result = new GenericResult<>();
    long startTime = System.currentTimeMillis(); 
	
    try {
        for (Users update : updates) {
            int id = update.getId();
            Query query = new Query(Criteria.where("id").is(id));

 
            if (mongoTemplate.exists(query, Users.class)) {
                Update updateQuery = new Update(); 
                String[] requiredFields = {
					    "firstName", "secondName", "password", "userPhone", "userEmail",
                         "customerId", "customerNumber", "userRole", "jobPosition",
                         "country", "street", "city", "state", "zip"
                                             
                }; 
                List<String> missingFields = new ArrayList<>(); 
                for (String field : requiredFields) {
                    Object fieldValue = getFieldValue(update, field); 
                    if (fieldValue == null || (fieldValue instanceof String && ((String) fieldValue).isEmpty())) {
                        missingFields.add(field);
                    } else {
                        updateQuery.set(field, fieldValue);

                    }

					LocalDateTime currentDateTime = LocalDateTime.now();
					updateQuery.set("UpdateDate", currentDateTime);
				    String updatedBy = getCurrentAuditor().get();
				    updateQuery.set("UpdateBy", updatedBy);
					
                }
 
                if (!missingFields.isEmpty()) {
                    result.setSuccess(false);
                    result.setMessage("Missing required fields for updating users with ID " + id + ": " +
                            String.join(", ", missingFields));
                    return result;
                }
 
                mongoTemplate.updateFirst(query, updateQuery, Users.class);
                logger.info("Users with ID {} updated successfully.", id);
            } else {
                result.setSuccess(false);
                result.setMessage("Users with ID " + id + " not found for update.");
                return result;
            }
        }
 
        result.setSuccess(true);
        result.setMessage("Users updated successfully.");
    } catch (Exception e) {
        handleException(result, e, "Error updating users");
        result.setSuccess(false);
        result.setMessage("Error updating users: " + e.getMessage());
        result.setErrorDetails(e.getMessage());
        logger.error("Error updating users. Error: {}", e.getMessage(), e);
    }
    long endTime = System.currentTimeMillis();
    logger.info("DB update Execution Time: {} ms", endTime - startTime);
    return result;
}

private Object getFieldValue(Users user, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    Field field = Users.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(user);
}
		@Override
		public GenericResult<String> deleteUser(List<Integer> delIds) {
			GenericResult<String> genericResult = new GenericResult<>();
			long startTime = System.currentTimeMillis();
			try {
				if (delIds.isEmpty()) {
					genericResult.setSuccess(false);
					genericResult.setMessage("Empty list provided. Please provide valid IDs for deletion.");
					return genericResult;
				}
				for (Integer id : delIds) {
					Query query = new Query(Criteria.where("id").is(id));
					if (mongoTemplate.exists(query, Users.class)) {
						mongoTemplate.remove(query, Users.class);
						genericResult.setSuccess(true);
						logger.info("User deleted: " + id);
					} else {
						genericResult.setSuccess(false);
						genericResult.setMessage("User with ID " + id + " not found for deletion.");
						return genericResult;
					}
				}
				genericResult.setMessage("User(s) deleted");
				return genericResult;
			} catch (Exception e) {
				handleException(genericResult, e, "Error deleting user(s)");
				logger.error("Error deleting user(s): " + e.getMessage());
			} finally {
				long endTime = System.currentTimeMillis();
				logger.info("DB deletion Execution Time: {} ms", endTime - startTime);
			}		 
			return genericResult;
		}
    private <T> void handleException(GenericResult<T> result, Exception e, String errorMessage) {
    logger.error(errorMessage, e);
    result.setSuccess(false);
    result.setMessage(errorMessage + ": " + e.getMessage());

	}
}