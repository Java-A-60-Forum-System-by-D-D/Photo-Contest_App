package com.example.demo.ServiceTests;// PhotoReviewServiceImplTest.java

import com.example.demo.exceptions.EntityDuplicateException;
import com.example.demo.models.*;
import com.example.demo.repositories.PhotoReviewRepository;
import com.example.demo.services.PhotoSubmissionService;
import com.example.demo.services.impl.PhotoReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoReviewServiceImplTest {

    private PhotoReviewServiceImpl photoReviewService;
    private PhotoReviewRepository photoReviewRepository;
    private PhotoSubmissionService photoSubmissionService;

    @BeforeEach
    void setUp() {
        photoReviewRepository = mock(PhotoReviewRepository.class);
        photoSubmissionService = mock(PhotoSubmissionService.class);
        photoReviewService = new PhotoReviewServiceImpl(photoReviewRepository, photoSubmissionService);
    }

    @Test
    void createPhotoReviewSavesReviewWhenValid() {
        PhotoReview photoReview = new PhotoReview();
        PhotoSubmission photoSubmission = new PhotoSubmission();
        photoSubmission.setReviews(new ArrayList<>());
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_2);
        photoSubmission.setContest(contest);

        when(photoReviewRepository.save(photoReview)).thenReturn(photoReview);

        PhotoReview result = photoReviewService.createPhotoReview(photoReview, photoSubmission);

        assertNotNull(result);
        assertEquals(photoReview, result);
        verify(photoSubmissionService).save(photoSubmission);
    }

    @Test
    void createPhotoReviewThrowsExceptionWhenContestNotInPhase2() {
        PhotoReview photoReview = new PhotoReview();
        PhotoSubmission photoSubmission = new PhotoSubmission();
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_1);
        photoSubmission.setContest(contest);

        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class, () -> {
            photoReviewService.createPhotoReview(photoReview, photoSubmission);
        });

        assertEquals(PhotoReviewServiceImpl.CONTEST_IS_NOT_IN_PHASE_2, exception.getMessage());
    }

    @Test
    void handleUserReviewThrowsExceptionWhenUserAlreadyReviewed() {
        User user = new User();
        PhotoReview photoReview = new PhotoReview();
        PhotoSubmission photoSubmission = new PhotoSubmission();

        when(photoReviewRepository.findPhotoReviewByJuryHasAlreadyReviewedPhotoSubmission(user.getId(), photoSubmission.id))
                .thenReturn(Optional.of(new PhotoReview()));

        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class, () -> {
            photoReviewService.handleUserReview(photoReview, photoSubmission, user);
        });

        assertEquals(PhotoReviewServiceImpl.USER_HAS_ALREADY_REVIEWED_THIS_PHOTO_SUBMISSION, exception.getMessage());
    }

    @Test
    void handleUserReviewSavesReviewWhenValid() {
        User user = new User();
        PhotoReview photoReview = new PhotoReview();
        PhotoSubmission photoSubmission = new PhotoSubmission();
        photoSubmission.setReviews(new ArrayList<>());
        Contest contest = new Contest();
        contest.setPhase(Phase.PHASE_2);
        photoSubmission.setContest(contest);

        when(photoReviewRepository.findPhotoReviewByJuryHasAlreadyReviewedPhotoSubmission(user.getId(), photoSubmission.id))
                .thenReturn(Optional.empty());
        when(photoReviewRepository.save(photoReview)).thenReturn(photoReview);

        PhotoReview result = photoReviewService.handleUserReview(photoReview, photoSubmission, user);

        assertNotNull(result);
        assertEquals(photoReview, result);
        verify(photoSubmissionService).save(photoSubmission);
    }
}