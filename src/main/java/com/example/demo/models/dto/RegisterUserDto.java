package com.example.demo.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;


}
