package com.example.demo.controllers.rest;

import com.example.demo.models.User;
import com.example.demo.models.dto.PhotoSubmissionDto;
import com.example.demo.models.dto.PhotoSubmissionViewDto;
import com.example.demo.models.mappers.PhotoMapper;
import com.example.demo.services.PhotoSubmissionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/photo-submissions")
public class PhotoSubmissionsRestController {
    private final PhotoSubmissionService photoSubmissionService;
    private final PhotoMapper photoMapper;

    public PhotoSubmissionsRestController(PhotoSubmissionService photoSubmissionService, PhotoMapper photoMapper) {
        this.photoSubmissionService = photoSubmissionService;
        this.photoMapper = photoMapper;
    }

    @GetMapping
    public List<PhotoSubmissionViewDto> getAllPhotoSubmissions() {
         return photoSubmissionService.getAllPhotoSubmissions().stream()
                 .map(photoMapper::toPhotoSubmissionViewDto)
                 .toList();

    }

}
