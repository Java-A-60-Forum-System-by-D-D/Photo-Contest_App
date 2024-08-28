package com.example.demo.models.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class PhotoSubmissionReviewsView {
    private String title;
    private String photoUrl;
    private List<ReviewView> reviews;

}
