package com.example.demo.services;

import com.example.demo.models.Category;

public interface CategoryService {
    Category getCategoryByName(String name);
    Category createCategory(String name, String description);

}
