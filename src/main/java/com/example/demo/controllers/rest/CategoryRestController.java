package com.example.demo.controllers.rest;

import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.models.dto.CategoryDTO;
import com.example.demo.models.mappers.CategoryMapper;
import com.example.demo.services.CategoryService;
import com.example.demo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/categories")
@Tag(name = "categories", description = "Category operations")
public class CategoryRestController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final UserService userService;

    public CategoryRestController(CategoryMapper categoryMapper, CategoryService categoryService, UserService userService) {
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created category"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public CategoryDTO createCategory(
            @Parameter(description = "Category details", required = true) @Valid @RequestBody CategoryDTO categoryDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        categoryService.createCategory(user, categoryDTO.getCategoryName(), categoryDTO.getCategoryDescription());
        return categoryDTO;
    }
}