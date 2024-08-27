package com.example.demo.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContestViewDto {
    private String title;
    private String description;
    private String category;
    private String creatorName;
    private LocalDateTime startPhase1;
    private LocalDateTime startPhase2;
    private LocalDateTime startPhase3;

}
