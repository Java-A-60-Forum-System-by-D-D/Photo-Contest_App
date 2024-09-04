package com.example.demo.controllers.mvc;


import com.example.demo.controllers.rest.CategoryRestController;
import com.example.demo.models.Category;
import com.example.demo.models.Contest;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoryService categoryService;
    private final ContestService contestService;



    @Autowired
    public CategoriesController(CategoryService categoryService, ContestService contestService) {
        this.categoryService = categoryService;
        this.contestService = contestService;

    }


    @GetMapping
    public String getAllCategories(Model model) {

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "categories";


    }


    @GetMapping("/{name}")
    public String getCategoryPage(@PathVariable String name, Model model) {

        Category category = categoryService.getCategoryByName(name);
        List<Contest> contests = contestService.getContestByCategoryName(category);



        model.addAttribute("category", category);
        model.addAttribute("contestsByCategory", contests);
        return "category";
    }


}
