package com.example.demo.services;

import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;

public interface PhotoReviewService {
    PhotoReview createPhotoReview(PhotoReview photoReview, PhotoSubmission photoSubmission);

    PhotoReview handleUserReview(PhotoReview photoReview, PhotoSubmission photoSubmission, User user);
}
