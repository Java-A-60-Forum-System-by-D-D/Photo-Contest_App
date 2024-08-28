package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "photo_reviews")
@Getter
@Setter
public class PhotoReview extends BaseEntity{
    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "category_mismatch", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean categoryMismatch;

    @ManyToOne
    private User jury;


    @Column(name = "is_reviewed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isReviewed;


}
