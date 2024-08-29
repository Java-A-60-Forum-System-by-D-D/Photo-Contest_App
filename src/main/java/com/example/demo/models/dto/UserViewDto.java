package com.example.demo.models.dto;

import lombok.Data;

@Data
public class UserViewDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private int totalScore;
}
