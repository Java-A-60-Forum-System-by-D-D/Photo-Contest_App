package com.example.demo.services.impl;

import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public LoginUserDto loginUser(LoginUserDto loginUserDto) {
//        AuthUser authUser = authenticationService.findByEmail(loginUserDto.getEmail());
        return null;
    }


}
