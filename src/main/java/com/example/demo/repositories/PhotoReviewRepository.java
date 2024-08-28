package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.PhotoReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoReviewRepository extends JpaRepository<PhotoReview, Long> {

    @Query("SELECT r FROM PhotoReview r JOIN PhotoSubmission ps ON r.id = ps.id WHERE r.jury.id = :userId AND ps.id = :photoSubmissionId")
    Optional<PhotoReview> findPhotoReviewByJuryHasAlreadyReviewedPhotoSubmission(@Param("userId") long userId, @Param("photoSubmissionId") long photoSubmissionId);

    @Query("SELECT sum(p.score) FROM PhotoReview p JOIN PhotoSubmission r ON r.id = p.id WHERE p.isReviewed = true AND r.id = :photoSubmissionId AND r.contest.id = :contestId")
    Integer findTotalScoreByPhotoSubmissionIdAndContestId(@Param("photoSubmissionId") long photoSubmissionId, @Param("contestId") long contestId);
}
