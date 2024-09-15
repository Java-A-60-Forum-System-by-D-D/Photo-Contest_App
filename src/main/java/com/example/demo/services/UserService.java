package com.example.demo.services;

import com.example.demo.models.Contest;
import com.example.demo.models.User;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.filtering.UserFilterOptions;

import java.util.List;

public interface UserService {
    User getUserById(long id);
    User saveUser(User user);


    User getUserByEmail(String email);

    User save(User user);
    List<User> findUsersByRoleOrganizer(String role);

    List<User> getUsers(UserFilterOptions userFilterOptions);

    User updateUser(User userToUpdate);

    User calculateLevel(User user);

    User getUserByUsername(String username);


    List<User> getAllUsers();

    List<User> getUsersByJurorContests(Contest contest);

    List<User> getUsersByRole();

    List<String> findUsernamesByTerm(String term);

    User updateFirstName(long userId, String firstName);
    User updateLastName(long userId, String lastName);
}
