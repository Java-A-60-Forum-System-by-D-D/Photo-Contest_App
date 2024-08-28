package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.Phase;
import com.example.demo.models.PhotoSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoSubmissionRepository extends JpaRepository<PhotoSubmission, Long> {

    @Query("Select p from PhotoSubmission p join PhotoReview r on p.id = r.id where r.isReviewed = true and r.jury.id = :id")
    List<PhotoSubmission> findByJuriIdAndReviewed(@Param("id") long id);

    @Query("Select p from PhotoSubmission p join PhotoReview  r on r.id = p.id where r.isReviewed = false")
    List<PhotoSubmission> findNotReviewedPhotos();

    @Query("Select p from PhotoSubmission p where p.creator.id = :userId and p.contest.phase = :phase")
    List<PhotoSubmission> findPhotoSubmissionsByCreator_IdAndContest_Phase_Finished(long userId, @Param("phase") Phase phase);

    @Query("Select p from PhotoSubmission  p where p.creator.id != :userId and p.contest.phase = :phase")
    List<PhotoSubmission> findPhotoSubmissionsCreatedByOtherUsers(long userId, @Param("phase") Phase phase);


}

