package com.example.demo.controllers.rest;

import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.models.dto.UserViewDto;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.RestAuthenticationServiceImpl;
import com.example.demo.services.schedulers.RestAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "Authentication operations")
public class AuthRestController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final RestAuthenticationService authenticationService;


        public AuthRestController(UserService userService, UserMapper userMapper, RestAuthenticationServiceImpl authenticationService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginUserDto> login(
            @Parameter(description = "Login details", required = true) @RequestBody LoginUserDto loginUserDto) {
        LoginUserDto response = authenticationService.loginUser(loginUserDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered user"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public UserViewDto registerUser(
            @Parameter(description = "Registration details", required = true) @Valid @RequestBody RegisterUserDto registerUserDto) {
        User user = userMapper.createUserFromDto(registerUserDto);
        userService.saveUser(user);
        return userMapper.mapUserToUserViewDto(user);
    }
}