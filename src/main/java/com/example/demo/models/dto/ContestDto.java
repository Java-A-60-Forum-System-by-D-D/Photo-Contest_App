package com.example.demo.models.dto;

import com.example.demo.models.Category;
import com.example.demo.models.Phase;
import com.example.demo.models.PhotoSubmission;
import com.example.demo.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ContestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String category;

    @NotNull
    private String type;

    private List<User> invitedUsers;

}
