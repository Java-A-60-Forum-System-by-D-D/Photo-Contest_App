package com.example.demo.controllers.mvc;

import com.example.demo.models.AuthUser;
import com.example.demo.models.User;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.UserService;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationMVCController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthenticationMVCController(UserService userService, UserMapper userMapper, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

//    @GetMapping("/login")
//    public String login(Model model) {
//
//        model.addAttribute("loginUserDto", new LoginUserDto());
//        return "sign-in-up";
//    }
//
//    @PostMapping("/login")
//    public String loginUser(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "sign-in-up";
//
//        }
//        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        model.addAttribute("user", loginUserDto);
//
//
//        return "index";
//    }
//    }
//    @PostMapping("/register")
//    public String registerUser(@Valid )
}
