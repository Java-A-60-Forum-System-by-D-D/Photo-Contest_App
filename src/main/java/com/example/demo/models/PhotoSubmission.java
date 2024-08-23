package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "photo_submissions")
@Getter
@Setter
public class PhotoSubmission extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "story", columnDefinition = "TEXT")
    private String story;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToMany
    @JoinTable(
            name = "photo_submission_reviews",
            joinColumns = @JoinColumn(name = "photo_submission_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_review_id")
    )
    private List<PhotoReview> reviews;




}
