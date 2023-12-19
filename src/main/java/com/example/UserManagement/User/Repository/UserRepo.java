// package com.example.UserManagement.User.Repository;
// import java.util.Date;
// import java.util.List;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
// import org.springframework.stereotype.Repository;
// import com.example.UserManagement.User.Entity.Users;

// @Repository
// public interface UserRepo extends MongoRepository<Users, Integer> {

//     Page<Users> findAll(Pageable pageable);

//     @Query("{ ?0 : ?1 }")
//     Page<Users> findByFilter(String key,  Object value, Pageable pageable);

//     @Query("{ 'firstName' : ?0, 'secondName' : ?1, 'password' : ?2, 'userPhone' : ?3, 'userEmail' : ?4, 'customerId' : ?5, 'customerNumber' : ?6, 'userRole' : ?7, 'jobPosition' : ?8, 'country' : ?9, 'street' : ?10, 'city' : ?11, 'state' : ?12, 'zip' : ?13, 'createDate' : ?14, 'updateDate' : ?15, 'createdBy' : ?16, 'updatedBy' : ?17}")
//     void createUser(String firstName, String secondName, String password, String userPhone, String userEmail, String customerId, String customerNumber, String userRole, String jobPosition, String country, String street, String city, String state, String zip, Date createDate, Date updateDate, String createdBy, String updatedBy);


//     @Query(value = "{'id': ?0}", fields = "{'$set':{ 'firstName' : ?0, 'secondName' : ?1, 'password' : ?2, 'userPhone' : ?3, 'userEmail' : ?4, 'customerId' : ?5, 'customerNumber' : ?6, 'userRole' : ?7, 'jobPosition' : ?8, 'country' : ?9, 'street' : ?10, 'city' : ?11, 'state' : ?12, 'zip' : ?13, 'createDate' : ?14, 'updateDate' : ?15, 'createdBy' : ?16, 'updatedBy' : ?17}")
//     void updateUserById(int id,String firstName, String secondName, String password, String userPhone, String userEmail, String customerId, String customerNumber, String userRole, String jobPosition, String country, String street, String city, String state, String zip, Date createDate, Date updateDate, String createdBy, String updatedBy);


//     @Query("{'id': {'$in': ?0}}")
//     void deleteUsers(List<Integer> ids);
// }

    
