package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.PhotoReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PhotoReviewRepository  extends JpaRepository<PhotoReview,Long> {

    @Query(nativeQuery = true, value = "SELECT R.ID as reviewId, PS.PHOTO_REVIEW_ID as submissionReviewId, P.ID as submissionId, R.* FROM PHOTO_REVIEWS R JOIN PHOTO_SUBMISSION_REVIEWS PS ON R.ID = PS.PHOTO_REVIEW_ID JOIN PHOTO_SUBMISSIONS P ON PS.PHOTO_SUBMISSION_ID = P.ID WHERE R.JURY_ID = :userId")
    Optional<PhotoReview> findPhotoReviewByJuryHasAlreadyReviewedPhotoSubmission(@Param("userId") long userId);

    @Query("SELECT sum(p.score) FROM PhotoReview p JOIN PhotoSubmission r ON r.id = p.id WHERE p.isReviewed = true AND r.id = :photoSubmissionId AND r.contest.id = :contestId")
    Integer findTotalScoreByPhotoSubmissionIdAndContestId(@Param("photoSubmissionId") long photoSubmissionId, @Param("contestId") long contestId);
}
