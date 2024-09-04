package com.example.demo.controllers.mvc;

import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserMVCDTO;
import com.example.demo.models.mappers.UserMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeMVCController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;




    public HomeMVCController(UserService userService, AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }
//    @ModelAttribute
//    public Role get

    @GetMapping(value = {"/home", "/"})
    public String home(Model model) {
        if (!model.containsAttribute("registerUser")) {
            model.addAttribute("registerUser", new RegisterUserMVCDTO());
        }
        if (!model.containsAttribute("loginUserDto")) {
            model.addAttribute("loginUserDto", new LoginUserDto());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("registerUser")) {
            model.addAttribute("registerUser", new RegisterUserMVCDTO());
        }
        if (!model.containsAttribute("loginUserDto")) {
            model.addAttribute("loginUserDto", new LoginUserDto());
        }
        return "index";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginUserDto") LoginUserDto loginUserDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        model.addAttribute("user", loginUserDto);
        User user = userService.getUserByEmail(loginUserDto.getEmail());
        UserRole role = user.getRole();
        model.addAttribute("role", role);
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerUser") RegisterUserMVCDTO registerUser, BindingResult bindingResult, Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (!model.containsAttribute("loginUserDto")) {
                model.addAttribute("loginUserDto", new LoginUserDto());
            }
            redirectAttributes.addFlashAttribute("registerUser", registerUser);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerUser", bindingResult);
            return "index";
        }
        User user = userMapper.createUserFromMVCDto(registerUser);
        userService.saveUser(user);
        return "redirect:/home";
    }
}
