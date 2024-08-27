package com.example.demo.models.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PhotoReviewDto {
    @NotNull
    @Min(1)
    @Max(10)
    private int score;
    @NotEmpty
    private String comment;
    private boolean categoryMismatch;
}
