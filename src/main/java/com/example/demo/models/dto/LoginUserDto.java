package com.example.demo.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotNull
    private String email;
    @NotNull
    private String password;

    private String token;

}
