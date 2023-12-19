package com.example.UserManagement.User.Controller;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.UserManagement.User.Entity.Users;
import com.example.UserManagement.User.Generics.GenericResult;
import com.example.UserManagement.User.Service.UserService;

    @RestController
    @RequestMapping("/users")
    public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired    
    private UserService userService;
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Map<String, Object> users,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(defaultValue = "username") String sortField,
                                      @RequestParam(defaultValue = "asc") String sortOrder) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("Received request to get users.");
            GenericResult<Page<Users>> result = userService.getUsers(users, page, size, sortField, sortOrder);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error getting users. Error: {}", e.getMessage(), e);
            long endTime = System.currentTimeMillis();
            logger.info(" GET API Response Time: {} ms", endTime - startTime);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting users: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createUsers(@RequestBody List<Users> newUsers) {
    long startTime = System.currentTimeMillis();
    try {
        logger.info("Received request to create users.");
        GenericResult<?> result = userService.createUser(newUsers);
        long endTime = System.currentTimeMillis();
        logger.info("DB Create Execution Time: {} ms", endTime - startTime);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return handleBadRequest(e);
    } catch (Exception e) {
        long endTime = System.currentTimeMillis();
        logger.error("DB Create Execution Time: {} ms", endTime - startTime);
        return handleInternalServerError(e);
    }
}
    @PutMapping
	public ResponseEntity<?> updateUser(@RequestBody List<Users> updates) {
    long startTime = System.currentTimeMillis();
		try {
			logger.info("Received request to update user");
			GenericResult<String> result = userService.updateUser(updates);
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			return handleBadRequest(e);
		} catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.info("DB updation Execution Time: {} ms", endTime - startTime);
			return handleInternalServerError(e);
		}
	}
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody List<Integer> delIds) {
 
    long startTime = System.currentTimeMillis();
    try {
        logger.info("Received request to delete users with IDs: {}", delIds);
        GenericResult<String> result = userService.deleteUser(delIds);
        long endTime = System.currentTimeMillis();
        logger.info("DB Delete Execution Time: {} ms", endTime - startTime);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return handleBadRequest(e);
    } catch (Exception e) {
        return handleInternalServerError(e);
    }
    }

    
  private ResponseEntity<String> handleBadRequest(Exception e) {
		logger.error("Bad request: {}", e.getMessage(), e);
		return ResponseEntity.badRequest().body("Bad request: " + e.getMessage());
	}
 
	private ResponseEntity<String> handleInternalServerError(Exception e) {
		logger.error("Internal server error: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
	}
    }


   