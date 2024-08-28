package com.example.demo.models.dto;

import lombok.Data;

import java.util.Map;
@Data
public class PhotoSubmissionReviewsView {
    private String title;
    private String photoUrl;
    private Map<String, Integer> reviewsScoreAndComments;
}
