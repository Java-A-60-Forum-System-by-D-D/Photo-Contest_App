package com.example.demo.services;

import com.example.demo.models.AuthUser;
import com.example.demo.models.dto.LoginUserDto;

import java.util.Optional;

public interface AuthenticationService {
    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByUsername(String username);

    LoginUserDto loginUser(LoginUserDto loginUserDto);

    AuthUser saveAuthUser(AuthUser authUser);
}
