package com.example.demo.services;

import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;

import java.util.List;

public interface PhotoSubmissionService {
    List<PhotoSubmission> getAllPhotoSubmissions();

    PhotoSubmission createPhotoSubmission(PhotoSubmission photoSubmission, User user);
}
