package com.hcltech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.service.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        CategoryRequestDTO requestDTO = CategoryRequestDTO.builder().categoryName("Electronics").build();
        CategoryResponseDTO responseDTO = CategoryResponseDTO.builder().categoryId(101L).categoryName("Electronics").build();

        Mockito.when(categoryService.createCategory(requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/category-api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName").value("Electronics"));

        Mockito.verify(categoryService, Mockito.times(1)).createCategory(requestDTO);
    }

    @Test
    void getAllCategory_ReturnsListOfCategories() throws Exception {
        // Arrange
        List<CategoryResponseDTO> responseList = Arrays.asList(
                CategoryResponseDTO.builder().categoryId(101L).categoryName("Electronics").build(),
                CategoryResponseDTO.builder().categoryId(102L).categoryName("Books").build()
        );

        Mockito.when(categoryService.getAllCategories()).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/category-api/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryId").value(101))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].categoryName").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryId").value(102))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].categoryName").value("Books"));

        Mockito.verify(categoryService, Mockito.times(1)).getAllCategories();
    }

    @Test
    void deleteCategoryById_ExistingId_ReturnsOkWithMessage() throws Exception {
        // Arrange
        Long categoryIdToDelete = 101L;
        String deletionMessage = "Category with ID " + categoryIdToDelete + " has been deleted successfully";

        Mockito.when(categoryService.deleteCategory(categoryIdToDelete)).thenReturn(deletionMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/category-api/delete/{categoryId}", categoryIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(deletionMessage));

        Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(categoryIdToDelete);
    }

    @Test
    void deleteCategoryById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Long nonExistingCategoryId = 999L;
        String notFoundMessage = "Category not found with ID: " + nonExistingCategoryId;

        Mockito.when(categoryService.deleteCategory(nonExistingCategoryId)).thenThrow(new com.hcltech.exceptions.CategoryNotFoundException(notFoundMessage));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/category-api/delete/{categoryId}", nonExistingCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(notFoundMessage));

        Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(nonExistingCategoryId);
    }
}
