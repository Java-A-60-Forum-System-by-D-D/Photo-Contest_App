package com.example.demo.repositories;

import com.example.demo.models.Category;
import com.example.demo.models.PhotoSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoSubmissionRepository  extends JpaRepository<PhotoSubmission,Long> {
}
