package com.example.demo.services;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;

public interface PhotoReviewService {
    PhotoReview createPhotoReview(PhotoReview photoReview, PhotoSubmission photoSubmission);
}
