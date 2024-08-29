package com.example.demo.models.filtering;


import lombok.Data;

@Data
public class UserFilterOptions {
    private String username;
    private String firstName;
    private String lastName;
    private String sortBy;
    private String sortDirection;


    public UserFilterOptions(String username, String firstName, String lastName, String sortBy, String sortDirection) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }


}