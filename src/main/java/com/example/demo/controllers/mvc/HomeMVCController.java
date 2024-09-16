package com.example.demo.controllers.mvc;

import com.example.demo.models.*;
import com.example.demo.models.dto.LoginUserDto;
import com.example.demo.models.dto.RegisterUserMVCDTO;
import com.example.demo.models.mappers.UserMapper;
import com.example.demo.services.ContestService;
import com.example.demo.services.NotificationService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class HomeMVCController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final NotificationService notificationService;
    private final ContestService contestService;

    private static final Logger logger = LoggerFactory.getLogger(HomeMVCController.class);


    public HomeMVCController(UserService userService, AuthenticationManager authenticationManager, UserMapper userMapper, NotificationService notificationService, ContestService contestService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;

        this.notificationService = notificationService;
        this.contestService = contestService;
    }
//    @ModelAttribute
//    public Role get

    @GetMapping(value = {"/home", "/"})
    public String home(Model model, Principal principal) {
        List<Notification> notifications = null;
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());

            if (user != null) {
                notifications = notificationService.getUnreadNotifications(user);
                model.addAttribute("notifications", notifications);
            }
        }
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.info("Current locale in home controller: " + currentLocale);
        if (!model.containsAttribute("registerUser")) {
            model.addAttribute("registerUser", new RegisterUserMVCDTO());
        }
        if (!model.containsAttribute("loginUserDto")) {
            model.addAttribute("loginUserDto", new LoginUserDto());
        }
        List<PhotoSubmission> photosToPopulate = new ArrayList<>();
        List<Contest> contestsToPopulate = contestService.getLast3FinishedContests();
        for (Contest contest : contestsToPopulate) {
            List<PhotoSubmission> submissions = contest.getSubmissions();
            TreeMap<Integer, List<User>> top3Map = contestService.calculateFinalContestPoints(submissions, userService.getAllUsers());
            List<PhotoSubmission> top3 = submissions.stream()
                    .filter(submission -> top3Map.keySet().contains(submission.getReviewScore()))
                    .limit(3)
                    .collect(Collectors.toList());
            for (PhotoSubmission photoSubmission : top3) {
                photosToPopulate.add(photoSubmission);
            }
        }

        model.addAttribute("photos", photosToPopulate);


        return "index";
    }

//    @GetMapping("/changeLanguage")
//    public String changeLanguage(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) {
//        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
//        if (localeResolver == null) {
//            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
//        }
//        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lang));
//        logger.info("Language changed to: " + lang);
//        logger.info("Current locale after setting: " + localeResolver.resolveLocale(request));
//        logger.info("Locale from LocaleContextHolder: " + LocaleContextHolder.getLocale());
//        return "redirect:/";
//    }
//    @GetMapping("/checkSession")
//    @ResponseBody
//    public String checkSession(HttpSession session) {
//        logger.info("Session id: {}", session.getId());
//        logger.info("Current locale: {}", LocaleContextHolder.getLocale());
//        return "Session checked. Check logs for details.";
//    }

    @ModelAttribute("notifications")
    public List<Notification> getNotifications(Principal principal, Model model) {

        List<Notification> notifications = null;
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());

            if (user != null) {
                notifications = notificationService.getUnreadNotifications(user);
                model.addAttribute("notifications", notifications);
            }
        }return notifications;
    }


    @GetMapping("/FAQ")
    public String getFAQ() {
        return "FAQ";
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
        SecurityContextHolder.getContext()
                             .setAuthentication(auth);
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
