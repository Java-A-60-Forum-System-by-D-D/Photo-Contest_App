package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.filtering.UserFilterOptions;

import java.util.List;

public interface UserService {
    User getUserById(long id);
    User saveUser(User user);


    User getUserByEmail(String email);

    void save(User user);
    List<User> findUsersByRoleOrganizer(String role);

    List<User> getUsers(UserFilterOptions userFilterOptions);

    User updateUser(User userToUpdate);

    User calculateLevel(User user);

    User getUserByUsername(String username);


}
