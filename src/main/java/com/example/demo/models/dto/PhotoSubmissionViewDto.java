package com.example.demo.models.dto;

import lombok.Data;

@Data
public class PhotoSubmissionViewDto {
    private String title;
    private String story;
    private String photoUrl;
    private String userNames;
    private String contestTitle;
}
