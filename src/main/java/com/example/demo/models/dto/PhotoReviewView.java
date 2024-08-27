package com.example.demo.models.dto;

import lombok.Data;

@Data
public class PhotoReviewView {
    private String photoUrl;
    private int score;
    private String comment;
    private boolean categoryMismatch;
    private String juryNames;
    private boolean isReviewed;
}
