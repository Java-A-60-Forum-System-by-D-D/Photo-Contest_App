package com.example.demo.services;

import com.example.demo.models.dto.LoginUserDto;

public interface RestAuthenticationService extends AuthenticationService {

    LoginUserDto loginUser(LoginUserDto loginUserDto);
}
