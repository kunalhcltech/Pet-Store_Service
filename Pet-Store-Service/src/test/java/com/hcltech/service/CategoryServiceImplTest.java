package com.hcltech.service;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.model.Category;
import com.hcltech.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId(1L)
                .categoryName("Fitness")
                .build();
    }

    @Test
    void testCreateCategory_Success() {
        CategoryRequestDTO request = new CategoryRequestDTO();
        request.setCategoryName("Fitness");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO response = categoryService.createCategory(request);

        assertNotNull(response);
        assertEquals("Fitness", response.getCategoryName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCategory_NullRequest_ThrowsException() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.createCategory(null));
    }

    @Test
    void testGetAllCategories_ReturnsList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();

        assertEquals(1, categories.size());
        assertEquals("Fitness", categories.get(0).getCategoryName());
    }

    @Test
    void testGetCategoryById_ValidId_ReturnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getCategoryId());
    }

    @Test
    void testGetCategoryById_InvalidId_ThrowsException() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(0L));
    }

    @Test
    void testGetCategoryById_NotFound_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testDeleteCategory_ValidId_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        String result = categoryService.deleteCategory(1L);

        assertEquals("Category with ID 1 has been deleted successfully", result);
        verify(categoryRepository).delete(category);
    }

    @Test
    void testDeleteCategory_InvalidId_ThrowsException() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(0L));
    }

    @Test
    void testDeleteCategory_NotFound_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
 