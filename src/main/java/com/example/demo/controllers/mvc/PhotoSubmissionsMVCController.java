package com.example.demo.controllers.mvc;

import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoReviewDto;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/submissions")
public class PhotoSubmissionsMVCController {
    private final PhotoSubmissionService photoSubmissionService;
    private final UserService userService;

    public PhotoSubmissionsMVCController(PhotoSubmissionService photoSubmissionService, UserService userService) {
        this.photoSubmissionService = photoSubmissionService;
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public String getReviewPage(@PathVariable long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("photoSubmission", photoSubmissionService.getPhotoSubmissionById(id));
        model.addAttribute("user", user);
        model.addAttribute("review", new PhotoReviewDto());

        return "photo-review";
    }
}
