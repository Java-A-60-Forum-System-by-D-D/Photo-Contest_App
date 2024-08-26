package com.example.demo.services.impl;

import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.models.AuthUser;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.services.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthUser findByEmail(String email) {

        return authUserRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(USER_DOES_NOT_EXIST));

    }
}
