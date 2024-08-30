package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.models.filtering.UserFilterOptions;
import com.example.demo.models.filtering.UserSpecification;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    public static final String USER_NOT_FOUND = "User not found: ";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUsersByEmail(email)
                                  .stream()
                                  .findFirst()
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return user;
    }

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;

    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                             .orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUsersByEmail(email)
                             .stream()
                             .findFirst()
                             .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + email));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findUsersByRoleOrganizer(String role) {
        return userRepository.findUsersByRole(UserRole.ORGANIZER);
    }

    @Override
    public List<User> getUsers(UserFilterOptions userFilterOptions) {
        Specification<User> spec = UserSpecification.filterByOptions(userFilterOptions);
        return userRepository.findAll(spec);
    }

    @Override
    public User updateUser(User userToUpdate) {

        return userRepository.save(userToUpdate);
    }

    @Override
    public User calculateLevel(User user) {
        int score = user.getTotalScore();

        if (score < 51) {
            user.setRole(UserRole.JUNKIE);
        } else if (score < 100) {
            user.setRole(UserRole.ENTHUSIAST);
        } else if (score < 151) {
            user.setRole(UserRole.MASTER);
        } else {
            user.setRole(UserRole.DICTATOR);
        }

        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                             .stream()
                             .findFirst()
                             .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + username));
    }


}
