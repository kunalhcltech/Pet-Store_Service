package com.hcltech.service;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.model.Category;
import com.hcltech.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_ValidInput_ReturnsResponseDTO() {
        // Arrange
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("Electronics").build();
        Category savedCategory = Category.builder().categoryId(101L).categoryName("Electronics").build();

        Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(savedCategory);

        // Act
        CategoryResponseDTO responseDTO = categoryService.createCategory(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(savedCategory.getCategoryId(), responseDTO.getCategoryId());
        assertEquals(savedCategory.getCategoryName(), responseDTO.getCategoryName());
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
    }

    @Test
    void createCategory_NullInput_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.createCategory(null));
        Mockito.verify(categoryRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAllCategories_ReturnsListOfResponseDTOs() {
        // Arrange
        List<Category> categories = Arrays.asList(
                Category.builder().categoryId(101L).categoryName("Electronics").build(),
                Category.builder().categoryId(102L).categoryName("Books").build()
        );
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<CategoryResponseDTO> responseDTOs = categoryService.getAllCategories();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals(categories.get(0).getCategoryId(), responseDTOs.get(0).getCategoryId());
        assertEquals(categories.get(0).getCategoryName(), responseDTOs.get(0).getCategoryName());
        assertEquals(categories.get(1).getCategoryId(), responseDTOs.get(1).getCategoryId());
        assertEquals(categories.get(1).getCategoryName(), responseDTOs.get(1).getCategoryName());
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getCategoryById_ExistingId_ReturnsResponseDTO() {
        // Arrange
        Long categoryId = 101L;
        Category category = Category.builder().categoryId(categoryId).categoryName("Electronics").build();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        CategoryResponseDTO responseDTO = categoryService.getCategoryById(categoryId);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(category.getCategoryId(), responseDTO.getCategoryId());
        assertEquals(category.getCategoryName(), responseDTO.getCategoryName());
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
    }

    @Test
    void getCategoryById_NonExistingId_ThrowsCategoryNotFoundException() {
        // Arrange
        Long categoryId = 999L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(categoryId));
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
    }

    @Test
    void getCategoryById_NullId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(null));
        Mockito.verify(categoryRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void getCategoryById_InvalidId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(-1L));
        Mockito.verify(categoryRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void deleteCategory_ExistingId_ReturnsSuccessMessage() {
        // Arrange
        Long categoryIdToDelete = 101L;
        Category categoryToDelete = Category.builder().categoryId(categoryIdToDelete).categoryName("Electronics").build();
        Mockito.when(categoryRepository.findById(categoryIdToDelete)).thenReturn(Optional.of(categoryToDelete));
        Mockito.doNothing().when(categoryRepository).delete(categoryToDelete);

        // Act
        String result = categoryService.deleteCategory(categoryIdToDelete);

        // Assert
        assertEquals("Category with ID " + categoryIdToDelete + " has been deleted successfully", result);
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryIdToDelete);
        Mockito.verify(categoryRepository, Mockito.times(1)).delete(categoryToDelete);
    }

    @Test
    void deleteCategory_NonExistingId_ThrowsCategoryNotFoundException() {
        // Arrange
        Long nonExistingCategoryId = 999L;
        Mockito.when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(nonExistingCategoryId));
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(nonExistingCategoryId);
        Mockito.verify(categoryRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteCategory_NullId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(null));
        Mockito.verify(categoryRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(categoryRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteCategory_InvalidId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(-1L));
        Mockito.verify(categoryRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(categoryRepository, Mockito.never()).delete(Mockito.any());
    }
}
