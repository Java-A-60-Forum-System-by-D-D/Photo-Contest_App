package com.example.demo.services.impl;

import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        AuthUser authUser = user.getAuthUser();
        authUser.setPassword(passwordEncoder.encode(authUser.getPassword()));
        authUser.setUsername(user.getAuthUser().getUsername());
        authUser.setEmail(user.getAuthUser().getEmail());
        user.setAuthUser(authUser);
        return userRepository.save(user);
    }
}
