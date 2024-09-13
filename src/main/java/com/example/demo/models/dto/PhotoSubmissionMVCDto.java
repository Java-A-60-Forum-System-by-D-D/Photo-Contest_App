package com.example.demo.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoSubmissionMVCDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String story;
    @NotEmpty
    private MultipartFile photoUrl;


}
