package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.PhotoSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PhotoSubmissionRepository  extends JpaRepository<PhotoSubmission,Long> {

    @Query("Select p from PhotoSubmission p join PhotoReview r on p.id = r.id where r.isReviewed = true and r.jury.id = :id")
    List<PhotoSubmission> findByJuriIdAndReviewed(@Param("id") long id);

    @Query("Select p from PhotoSubmission p join PhotoReview  r on r.id = p.id where r.isReviewed = false")
    List<PhotoSubmission> findNotReviewedPhotos();

    List<PhotoSubmission> findPhotoSubmissionByContest_IdAndCreator_Id(long contestId, long userId);
@Query("Select p from PhotoSubmission  p where p.contest.id = :contestId and p.creator.id != :userId")
    List<PhotoSubmission> findPhotoSubmissionsByContest_Id(long contestId, long userId);
}
