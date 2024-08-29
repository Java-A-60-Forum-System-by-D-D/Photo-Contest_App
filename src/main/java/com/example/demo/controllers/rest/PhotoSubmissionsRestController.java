package com.example.demo.controllers.rest;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoReviewDto;
import com.example.demo.models.dto.PhotoReviewView;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import com.example.demo.models.filtering.PhotoSubmissionFilterOptions;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.models.mappers.ReviewMapper;
import com.example.demo.services.PhotoReviewService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/photo-submissions")
public class PhotoSubmissionsRestController {
    private final PhotoSubmissionService photoSubmissionService;
    private final PhotoMapper photoMapper;
    private final UserService userService;
    private final ReviewMapper reviewMapper;
    private final PhotoReviewService photoReviewService;

    public PhotoSubmissionsRestController(PhotoSubmissionService photoSubmissionService, PhotoMapper photoMapper, UserService userService, ReviewMapper reviewMapper, PhotoReviewService photoReviewService) {
        this.photoSubmissionService = photoSubmissionService;
        this.photoMapper = photoMapper;
        this.userService = userService;
        this.reviewMapper = reviewMapper;
        this.photoReviewService = photoReviewService;
    }


    @GetMapping
    public List<PhotoSubmissionViewDto> getAllPhotoSubmissions(@RequestParam(required = false) String title,
                                                               @RequestParam(required = false) String username,
                                                               @RequestParam(required = false) String sortBy,
                                                               @RequestParam(required = false) String sortDirection) {
        PhotoSubmissionFilterOptions filterOptions = new PhotoSubmissionFilterOptions(title, username, sortBy, sortDirection);
        return photoSubmissionService.getPhotoSubmissions(filterOptions)
                                     .stream()
                                     .map(photoMapper::toPhotoSubmissionViewDto)
                                     .toList();
    }

//    @GetMapping("/{id}")
//    public PhotoSubmissionViewDto getPhotoSubmission(@PathVariable int id) {
//
//    }

    @GetMapping("/{id}/reviews")
    public Map<String, List<PhotoSubmissionViewDto>> getPhotoSubmissionReviews(@PathVariable long id) {
        PhotoSubmission photoSubmission = photoSubmissionService.getPhotoSubmissionById(id);
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        List<PhotoSubmissionViewDto> reviewed = photoSubmissionService.getAllReviewedPhotos(photoSubmission, user)
                                                                      .stream()
                                                                      .map(photoMapper::toPhotoSubmissionViewDto)
                                                                      .toList();
        List<PhotoSubmissionViewDto> notReviewed = photoSubmissionService.getNotReviewedPhotos()
                                                                         .stream()
                                                                         .map(photoMapper::toPhotoSubmissionViewDto)
                                                                         .toList();
        return Map.of("reviewed", reviewed, "notReviewed", notReviewed);

    }

    @PostMapping("/{id}/reviews")
    public PhotoReviewView createReview(@PathVariable long id, @RequestBody @Valid PhotoReviewDto photoReviewDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        PhotoSubmission photoSubmission = photoSubmissionService.getPhotoSubmissionById(id);
        PhotoReview photoReview = reviewMapper.createPhotoReviewFromDto(photoReviewDto, user);
        return reviewMapper.toPhotoReviewView(photoReviewService.handleUserReview(photoReview, photoSubmission, user), photoSubmission);

    }


}
