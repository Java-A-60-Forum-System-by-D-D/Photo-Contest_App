package com.example.demo.models.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class CategoryDTO {

    @NotEmpty
    private String categoryName;

    @NotEmpty
    private String categoryDescription;

}
