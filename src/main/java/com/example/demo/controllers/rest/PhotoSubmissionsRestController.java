package com.example.demo.controllers.rest;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoReviewDto;
import com.example.demo.models.dto.PhotoReviewView;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import com.example.demo.models.filtering.PhotoSubmissionFilterOptions;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.models.mappers.ReviewMapper;
import com.example.demo.services.PhotoReviewService;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photo-submissions")
@Tag(name = "photo-submissions", description = "Photo submission operations")
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

    @Operation(summary = "View a list of available photo submissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<PhotoSubmissionViewDto> getAllPhotoSubmissions(
            @Parameter(description = "Title to filter photo submissions by", required = false) @RequestParam(required = false) String title,
            @Parameter(description = "Username to filter photo submissions by", required = false) @RequestParam(required = false) String username,
            @Parameter(description = "Field to sort photo submissions by", required = false) @RequestParam(required = false) String sortBy,
            @Parameter(description = "Direction to sort photo submissions by", required = false) @RequestParam(required = false) String sortDirection) {
        PhotoSubmissionFilterOptions filterOptions = new PhotoSubmissionFilterOptions(title, sortBy, sortDirection, username);
        return photoSubmissionService.getPhotoSubmissions(filterOptions)
                .stream()
                .map(photoMapper::toPhotoSubmissionViewDto)
                .toList();
    }

    @Operation(summary = "View reviews for a specific photo submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews"),
            @ApiResponse(responseCode = "404", description = "Photo submission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/reviews")
    public Map<String, List<PhotoSubmissionViewDto>> getPhotoSubmissionReviews(
            @Parameter(description = "ID of the photo submission to get reviews for", required = true) @PathVariable long id) {
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

    @Operation(summary = "Create a review for a specific photo submission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created review"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Photo submission not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/reviews")
    public PhotoReviewView createReview(
            @Parameter(description = "ID of the photo submission to review", required = true) @PathVariable long id,
            @Parameter(description = "Review details", required = true) @RequestBody @Valid PhotoReviewDto photoReviewDto) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        PhotoSubmission photoSubmission = photoSubmissionService.getPhotoSubmissionById(id);
        PhotoReview photoReview = reviewMapper.createPhotoReviewFromDto(photoReviewDto, user);
        return reviewMapper.toPhotoReviewView(photoReviewService.handleUserReview(photoReview, photoSubmission, user), photoSubmission);
    }
}