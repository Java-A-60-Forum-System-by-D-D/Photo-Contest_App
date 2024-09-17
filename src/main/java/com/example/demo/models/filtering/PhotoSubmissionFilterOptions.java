package com.example.demo.models.filtering;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhotoSubmissionFilterOptions {
    private String title;
    private String sortBy;
    private String sortDirection;

    // Constructors, getters, and setters
    public PhotoSubmissionFilterOptions(String title, String sortBy, String sortDirection, String username) {
        this.title = title;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }
}
