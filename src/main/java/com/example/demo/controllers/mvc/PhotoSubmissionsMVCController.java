package com.example.demo.controllers.mvc;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoReviewDto;
import com.example.demo.models.dto.PhotoSubmissionReviewsView;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.models.mappers.ReviewMapper;
import com.example.demo.services.PhotoReviewService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/submissions")
public class PhotoSubmissionsMVCController {
    private final PhotoSubmissionService photoSubmissionService;
    private final UserService userService;
    private final ReviewMapper reviewMapper;
    private final PhotoReviewService photoReviewService;
    private final PhotoMapper photoMapper;

    public PhotoSubmissionsMVCController(PhotoSubmissionService photoSubmissionService, UserService userService, ReviewMapper reviewMapper, PhotoReviewService photoReviewService, PhotoMapper photoMapper) {
        this.photoSubmissionService = photoSubmissionService;
        this.userService = userService;
        this.reviewMapper = reviewMapper;
        this.photoReviewService = photoReviewService;
        this.photoMapper = photoMapper;
    }

    @GetMapping("/{id}")
    public String getReviewPage(@PathVariable long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("photoSubmission", photoSubmissionService.getPhotoSubmissionById(id));
        model.addAttribute("user", user);
        model.addAttribute("review", new PhotoReviewDto());


        return "photo-review";
    }

    @PostMapping("/{id}")
    public String submitPhotoReview(@PathVariable long id,
                                    Model model,
                                    Principal principal,
                                    @Valid @ModelAttribute("review") PhotoReviewDto photoReviewDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "photo-review";
        }
        PhotoSubmission photoSubmission = photoSubmissionService.getPhotoSubmissionById(id);
        Contest contest = photoSubmission.getContest();
        User user = userService.getUserByEmail(principal.getName());
        PhotoReview photoReview = reviewMapper.createPhotoReviewFromDto(photoReviewDto, user);
        photoReviewService.handleUserReview(photoReview, photoSubmission, user);
        model.addAttribute("id", contest.getId());

        return "redirect:/contests/" + String.valueOf(contest.getId());


    }
    @GetMapping("/{id}/reviews")
    public String getPhotoSubmissionReviews(@PathVariable Long id, Model model) {
        PhotoSubmission photoSubmission = photoSubmissionService.getPhotoSubmissionById(id);
        PhotoSubmissionReviewsView photoSubmissionReviewsView = photoMapper.createPhotoSubmissionReviewView(photoSubmission);
        model.addAttribute("photoSubmissionReviewsView", photoSubmissionReviewsView);
        model.addAttribute("totalScore", photoSubmission.getReviewScore());
        return "photo-submission-reviews";
    }
}
