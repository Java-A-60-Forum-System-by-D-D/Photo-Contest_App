package com.example.demo.controllers.rest;

import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    public AuthRestController(UserService userService, UserMapper userMapper, AuthenticationService authenticationService) {
        this.userService = userService;
        this.userMapper = userMapper;

        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public AuthUser login(@RequestBody LoginUserDto loginUserDto) {
        AuthUser user = authenticationService.loginUser(loginUserDto);
        return user;
    }

    @PostMapping("/register")
    public UserViewDto registerUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = userMapper.createUserFromDto(registerUserDto);

        userService.saveUser(user);

        return userMapper.mapUserToUserViewDto(user);
    }
}
