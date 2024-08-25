package com.example.demo.controllers.rest;

import com.example.demo.models.dto.RegisterUserDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/register")
    public String register(){
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@RequestBody RegisterUserDto registerUserDto){

        return "register";
    }
}
