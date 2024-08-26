package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;

public interface UserService {
    User getUserById(long id);
    User saveUser(RegisterUserDto registerUserDto);

    LoginUserDto loginUser(LoginUserDto loginUserDto);
}
