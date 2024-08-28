package com.example.demo.models.dto;

import lombok.Data;

@Data
public class ReviewView {
    private int score;
    private String comment;
    private String juryNames;
}
