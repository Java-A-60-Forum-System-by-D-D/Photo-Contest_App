package com.example.demo.models.mappers;

import com.example.demo.models.Category;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {


    public Category createFromDTO(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getCategoryDescription());

        return category;

    }

}
