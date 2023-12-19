package com.example.UserManagement.User.Service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.UserManagement.User.Entity.Users;
import com.example.UserManagement.User.Generics.GenericResult;


public interface UserService {

    GenericResult<Users> createUser(List<Users> newUsers);
    GenericResult<String> updateUser(List<Users> updates);
    GenericResult<Page<Users>> getUsers(Map<String, Object> users, int page, int size, String sortField, String sortOrder);
    GenericResult<String> deleteUser(List<Integer> delIds);
}
    

