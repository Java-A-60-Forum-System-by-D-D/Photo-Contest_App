package com.example.demo.services;

import com.example.demo.models.dto.LoginUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MVCAuthenticationService extends AuthenticationService {

    LoginUserDto loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response);

    void logoutUser(HttpServletRequest request, HttpServletResponse response);

}
