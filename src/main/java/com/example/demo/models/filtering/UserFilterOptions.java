package com.example.demo.models.filtering;


import com.example.demo.models.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserFilterOptions {
    private String email;
    private String firstName;
    private String lastName;
    private String sortBy;
    private String sortDirection;
    private String role;


    public UserFilterOptions(String email, String firstName, String lastName, String sortBy, String sortDirection, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.role = role;
    }


}