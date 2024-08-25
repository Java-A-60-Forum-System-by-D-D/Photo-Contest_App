package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.PhotoReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoReviewRepository  extends JpaRepository<PhotoReview,Long> {
}
