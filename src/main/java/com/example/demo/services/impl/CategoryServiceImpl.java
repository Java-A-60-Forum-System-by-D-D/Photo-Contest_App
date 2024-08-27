package com.example.demo.services.impl;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Service
public class CategoryServiceImpl implements CategoryService {
    public static final String ONLY_ORGANIZERS_CAN_CREATE_CATEGORIES = "Only organizers can create categories";
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryByName(String name) {

        return categoryRepository.findCategoriesByName(name)
                                 .stream()
                                 .findFirst()
                                 .orElseThrow(() -> new EntityNotFoundException("No category with such name"));
    }

    @Override
    public Category createCategory(User user, String name, String description) {


        if (!user.getRole()
                 .toString()
                 .equalsIgnoreCase("Organizer")) {
            throw new AuthorizationUserException(ONLY_ORGANIZERS_CAN_CREATE_CATEGORIES);
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }


}
