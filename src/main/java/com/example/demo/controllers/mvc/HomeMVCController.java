package com.example.demo.controllers.mvc;

import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class HomeMVCController {
    private final UserService userService;


    public HomeMVCController(UserService userService) {
        this.userService = userService;

    }
    @GetMapping(value = {"/home", "/"})
    public String home(Model model) {
        model.addAttribute("loginUserDto", new LoginUserDto());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("loginUserDto", new LoginUserDto());
        return "index";
    }

//    @PostMapping("/login")
//    public String loginUser(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "index";
//
//        }
//        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        model.addAttribute("user", loginUserDto);
//
//
//        return "index";
//    }
}
