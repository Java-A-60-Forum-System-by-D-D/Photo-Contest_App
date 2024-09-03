package com.example.demo.controllers.mvc;


import com.example.demo.models.Category;
import com.example.demo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoryService categoryService;


    @Autowired
    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping
    public String getAllCategories(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "categories";


    }


}
