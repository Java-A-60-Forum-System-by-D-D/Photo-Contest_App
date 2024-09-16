package com.example.demo.models.filtering;

import com.example.demo.models.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
@Data
@NoArgsConstructor
public class OptionalUserFilteringOptions {
    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> sortBy;
    private Optional<String> sortDirection;
    private Optional<UserRole> role;

    public OptionalUserFilteringOptions(String email, String firstName, String lastName, String sortBy, String sortDirection, UserRole role) {
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortDirection = Optional.ofNullable(sortDirection);
        this.role = Optional.ofNullable(role);
    }
}
