package com.example.demo.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JuryDTO {

    @NotEmpty
    String email;

}
