package com.example.demo.services.impl;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.repositories.PhotoReviewRepository;
import com.example.demo.services.PhotoReviewService;
import com.example.demo.services.PhotoSubmissionService;
import org.springframework.stereotype.Service;

@Service
public class PhotoReviewServiceImpl implements PhotoReviewService {
    private final PhotoReviewRepository photoReviewRepository;
    private final PhotoSubmissionService photoSubmissionService;

    public PhotoReviewServiceImpl(PhotoReviewRepository photoReviewRepository, PhotoSubmissionService photoSubmissionService) {
        this.photoReviewRepository = photoReviewRepository;
        this.photoSubmissionService = photoSubmissionService;
    }

    @Override
    public PhotoReview createPhotoReview(PhotoReview photoReview, PhotoSubmission photoSubmission) {
        PhotoReview savedPhotoReview = photoReviewRepository.save(photoReview);

        photoSubmission.getReviews().add(savedPhotoReview);
        photoSubmissionService.save(photoSubmission);

        return savedPhotoReview;
    }
}
