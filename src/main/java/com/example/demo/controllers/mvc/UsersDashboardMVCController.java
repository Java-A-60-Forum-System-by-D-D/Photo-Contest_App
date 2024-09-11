package com.example.demo.controllers.mvc;

import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.models.dto.ContestSummaryDTO;
import com.example.demo.models.mappers.ContestMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UsersDashboardMVCController {
    private final UserService userService;
    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final CategoryService categoryService;

    public UsersDashboardMVCController(UserService userService, ContestService contestService, ContestMapper contestMapper, CategoryService categoryService) {
        this.userService = userService;
        this.contestService = contestService;
        this.contestMapper = contestMapper;
        this.categoryService = categoryService;
    }
    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/openContests")
    public String openContests(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<ContestSummaryDTO> openContests = contestService.getPhaseOneContestsAndTypeOpen()
                .stream()
                .map(contestMapper::createFinishedContestViewDto)
                .toList();
        model.addAttribute("contests", openContests);

        return "user-open-contests";
    }

}
