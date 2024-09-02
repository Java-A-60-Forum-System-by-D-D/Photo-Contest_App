package com.example.demo.models.dto;


import com.example.demo.models.validations.FieldMatch;
import com.example.demo.models.validations.UniqueUserEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "The passwords do not match")
public class RegisterUserMVCDTO {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotEmpty(message = "User email should be provided.")
    @Email(message = "User email should be valid.")
    @UniqueUserEmail(message = "User email should be unique.")
    private String email;


}
