package com.example.demo.services;

import com.example.demo.models.User;

public interface UserService {
    User getUserById(long id);
    User saveUser(User user);
}
