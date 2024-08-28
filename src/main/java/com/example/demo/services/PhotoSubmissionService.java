package com.example.demo.services;

import com.example.demo.models.Contest;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;

import java.util.List;

public interface PhotoSubmissionService {
    List<PhotoSubmission> getAllPhotoSubmissions();

    PhotoSubmission createPhotoSubmission(PhotoSubmission photoSubmission, User user);

    List<PhotoSubmission> getAllReviewedPhotos(PhotoSubmission photoSubmission, User user);

    PhotoSubmission getPhotoSubmissionById(long id);

    List<PhotoSubmission> getNotReviewedPhotos();

    void save(PhotoSubmission photoSubmission);

    List<PhotoSubmission> getAScoreAndComments( User user);

    List<PhotoSubmission> findAllContestSubmissionsByOthers( User user);
}
