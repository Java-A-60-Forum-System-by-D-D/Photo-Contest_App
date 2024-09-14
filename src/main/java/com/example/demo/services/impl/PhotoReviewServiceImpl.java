package com.example.demo.services.impl;

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.models.Phase;
import com.example.demo.models.PhotoReview;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import com.example.demo.repositories.PhotoReviewRepository;
import com.example.demo.services.PhotoReviewService;
import com.example.demo.services.PhotoSubmissionService;
import org.springframework.stereotype.Service;

@Service
public class PhotoReviewServiceImpl implements PhotoReviewService {
    public static final String USER_HAS_ALREADY_REVIEWED_THIS_PHOTO_SUBMISSION = "User has already reviewed this photo submission";
    public static final String CONTEST_IS_NOT_IN_PHASE_2 = "Contest is not in phase 2";
    private final PhotoReviewRepository photoReviewRepository;
    private final PhotoSubmissionService photoSubmissionService;

    public PhotoReviewServiceImpl(PhotoReviewRepository photoReviewRepository, PhotoSubmissionService photoSubmissionService) {
        this.photoReviewRepository = photoReviewRepository;
        this.photoSubmissionService = photoSubmissionService;
    }

    @Override
    public PhotoReview createPhotoReview(PhotoReview photoReview, PhotoSubmission photoSubmission) {
        if(!photoSubmission.getContest().getPhase().equals(Phase.PHASE_2)){
            throw new EntityDuplicateException(CONTEST_IS_NOT_IN_PHASE_2);
        }
        PhotoReview savedPhotoReview = photoReviewRepository.save(photoReview);

        photoSubmission.getReviews()
                       .add(savedPhotoReview);
        photoSubmissionService.save(photoSubmission);

        return savedPhotoReview;
    }


    @Override
    public PhotoReview handleUserReview(PhotoReview photoReview, PhotoSubmission photoSubmission, User user) {
        if (CheckIfUserAlreadyReviewed(user,photoSubmission)) {
            throw new EntityDuplicateException(USER_HAS_ALREADY_REVIEWED_THIS_PHOTO_SUBMISSION);
        }


        return createPhotoReview(photoReview, photoSubmission);
    }

    public boolean CheckIfUserAlreadyReviewed(User user, PhotoSubmission photoSubmission) {

        if (photoReviewRepository.findPhotoReviewByJuryHasAlreadyReviewedPhotoSubmission(user.getId(),photoSubmission.id)
                                 .isPresent()) {
            return true;
        }

        return false;
    }

}
