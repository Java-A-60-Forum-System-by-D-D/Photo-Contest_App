package com.example.demo.models.dto;

import com.example.demo.models.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PhotoSubmissionDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String story;
    @NotEmpty
    private String photoUrl;


}
