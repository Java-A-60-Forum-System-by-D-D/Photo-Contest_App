package com.example.demo.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDto {
    @NotEmpty
    private String name;
    @NotEmpty

    private String phone;
    @NotEmpty

    private String email;
    @NotEmpty

    private String subject;
    @NotEmpty

    private String message;
}
