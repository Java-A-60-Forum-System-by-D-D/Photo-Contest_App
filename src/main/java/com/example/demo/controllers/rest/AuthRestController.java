package com.example.demo.controllers.rest;

import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserDto;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
   private final UserService userService;

    public AuthRestController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/login")
    public Principal login(Principal principal){

        return principal;
    }
//    @PostMapping`("/login")
//    public LoginUserDto login(@RequestBody LoginUserDto loginUserDto){
//        userService.loginUser(loginUserDto);
//        return "login";
//    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterUserDto registerUserDto){
        userService.saveUser(registerUserDto);

        return "register";
    }
}
