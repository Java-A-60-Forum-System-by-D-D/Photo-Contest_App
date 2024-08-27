package com.example.demo.controllers.rest;


import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.models.mappers.CategoryMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
public class CategoryRestController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryRestController(CategoryMapper categoryMapper, CategoryService categoryService, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @PostMapping
    public CategoryDTO createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {

        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();

        User user = userService.getUserByEmail(authentication.getName());
        categoryService.createCategory(user,categoryDTO.getCategoryName(), categoryDTO.getCategoryDescription());
        return categoryDTO;

    }


}
