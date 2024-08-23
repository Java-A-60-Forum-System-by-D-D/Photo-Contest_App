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
public class PhotoSubmission extends BaseEntity{
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "story", columnDefinition = "TEXT")
    private String story;
    @Column(name = "photo_url", nullable = false)
    private String photoUrl;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Contest contest;
    @OneToMany(mappedBy = "photoSubmission")
    List<PhotoReview> reviews;



}
