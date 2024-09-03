package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.Type;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestDto;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class OrganizerDashboardMVCController {
    private final CategoryService categoryService;
    private final ContestService contestService;
    private final UserService userService;
    private final ContestMapper contestMapper;

    public OrganizerDashboardMVCController(CategoryService categoryService, ContestService contestService, UserService userService, ContestMapper contestMapper) {
        this.categoryService = categoryService;
        this.contestService = contestService;
        this.userService = userService;
        this.contestMapper = contestMapper;
    }


    @GetMapping("/createContest")
    public String createContest(Model model) {
        model.addAttribute("contestDto", new ContestDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("types", Type.values());
        return "create-contest";
    }
    @PostMapping("/createContest")
    public String createContest(@Valid @ModelAttribute("contestDto") ContestDto contestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                Model model, Principal principal) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.contestDto", bindingResult);
            redirectAttributes.addFlashAttribute("contestDto", contestDto);
            return "create-contest";
        }
        User user = userService.getUserByEmail(principal.getName());
        Contest contest = contestMapper.createContestFromDto(contestDto, user);

        contestService.createContest(contest, user);

        return "redirect:/home";
    }
}
