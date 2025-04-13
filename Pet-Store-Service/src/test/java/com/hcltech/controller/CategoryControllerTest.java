package com.hcltech.controller;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.service.CategoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ SUCCESS: Create category
    @Test
    void testCreateCategory() {
        CategoryRequestDTO request = new CategoryRequestDTO();
        CategoryResponseDTO response = new CategoryResponseDTO();

        when(categoryService.createCategory(request)).thenReturn(response);

        ResponseEntity<CategoryResponseDTO> result = categoryController.createCategory(request);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response);
        verify(categoryService, times(1)).createCategory(request);
    }

    // ✅ SUCCESS: Get all categories
    @Test
    void testGetAllCategories() {
        List<CategoryResponseDTO> responseList = List.of(new CategoryResponseDTO(), new CategoryResponseDTO());

        when(categoryService.getAllCategories()).thenReturn(responseList);

        ResponseEntity<List<CategoryResponseDTO>> result = categoryController.getAllCategory();

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(2);
        verify(categoryService, times(1)).getAllCategories();
    }

    // ✅ SUCCESS: Delete category
    @Test
    void testDeleteCategory() {
        Long id = 1L;
        String message = "Category deleted";

        when(categoryService.deleteCategory(id)).thenReturn(message);

        ResponseEntity<String> result = categoryController.deleteCategoryById(id);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(message);
        verify(categoryService, times(1)).deleteCategory(id);
    }

    // ⚠️ EXCEPTION: createCategory throws RuntimeException
    @Test
    void testCreateCategoryThrowsException() {
        CategoryRequestDTO request = new CategoryRequestDTO();

        when(categoryService.createCategory(request)).thenThrow(new RuntimeException("DB failure"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            categoryController.createCategory(request);
        });

        assertThat(ex.getMessage()).isEqualTo("DB failure");
        verify(categoryService, times(1)).createCategory(request);
    }

    // ⚠️ EXCEPTION: getAllCategory throws RuntimeException
    @Test
    void testGetAllCategoryThrowsException() {
        when(categoryService.getAllCategories()).thenThrow(new RuntimeException("Service down"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            categoryController.getAllCategory();
        });

        assertThat(ex.getMessage()).isEqualTo("Service down");
        verify(categoryService, times(1)).getAllCategories();
    }

    // ⚠️ EXCEPTION: deleteCategory throws CategoryNotFoundException
    @Test
    void testDeleteCategoryThrowsCategoryNotFoundException() {
        Long id = 404L;
        when(categoryService.deleteCategory(id)).thenThrow(new CategoryNotFoundException("Category not found"));

        CategoryNotFoundException ex = assertThrows(CategoryNotFoundException.class, () -> {
            categoryController.deleteCategoryById(id);
        });

        assertThat(ex.getMessage()).isEqualTo("Category not found");
        verify(categoryService, times(1)).deleteCategory(id);
    }
}