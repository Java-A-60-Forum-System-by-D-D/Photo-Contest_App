package com.example.demo.services;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {
    User getUserById(long id);
    User saveUser(User user);



    User getUserByEmail(String email);

    void save(User user);
    List<User> findUsersByRoleOrganizer(String role);
}
