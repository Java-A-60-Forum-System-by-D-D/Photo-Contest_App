package com.example.demo.ServiceTests;

import com.example.demo.exceptions.AuthorizationUserException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Category;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategoryByNameReturnsCategoryWhenExists() {
        Category category = new Category();
        category.setName("Test Category");
        when(categoryRepository.findCategoriesByName("Test Category")).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryByName("Test Category");

        assertNotNull(result);
        assertEquals("Test Category", result.getName());
    }

    @Test
    void getCategoryByNameThrowsExceptionWhenNotExists() {


        when(categoryRepository.findCategoriesByName("Nonexistent Category")).thenThrow(new EntityNotFoundException("Nonexistent Category"));

        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryByName("Nonexistent Category"));
    }

    @Test
    void createCategoryThrowsExceptionWhenUserNotOrganizer() {
        User user = new User();
        user.setRole(UserRole.JUNKIE);

        assertThrows(AuthorizationUserException.class, () -> categoryService.createCategory(user, "New Category", "Description"));
    }


    @Test
    void createCategorySavesCategoryWhenUserIsOrganizer() {
        User user = new User();
        user.setRole(UserRole.ORGANIZER);
        Category category = new Category();
        category.setName("New Category");
        category.setDescription("Description");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(user, "New Category", "Description");

        assertNotNull(result);
        assertEquals("New Category", result.getName());
        assertEquals("Description", result.getDescription());
    }

    @Test
    void getAllCategoriesReturnsListOfCategories() {
        Category category1 = new Category();
        category1.setName("Category 1");
        Category category2 = new Category();
        category2.setName("Category 2");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        assertEquals("Category 2", result.get(1).getName());
    }
}