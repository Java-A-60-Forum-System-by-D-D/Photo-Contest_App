package com.example.demo.services.schedulers;

import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.services.AuthenticationService;

public interface RestAuthenticationService extends AuthenticationService {

    LoginUserDto loginUser(LoginUserDto loginUserDto);
}
