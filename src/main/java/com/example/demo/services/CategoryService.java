package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.models.User;

import java.util.List;

public interface CategoryService {
    Category getCategoryByName(String name);
    Category createCategory(User user, String name, String description);

    List<Category> getAllCategories();
}
