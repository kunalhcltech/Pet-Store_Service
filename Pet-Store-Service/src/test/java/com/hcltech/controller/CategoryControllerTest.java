package com.hcltech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateCategory() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setCategoryName("Food");

        CategoryResponseDTO responseDTO = CategoryResponseDTO.builder()
                .categoryId(1L)
                .categoryName("Food")
                .build();

        when(categoryService.createCategory(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/category-api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.categoryName").value("Food"));
    }

    @Test
    void testGetAllCategories() throws Exception {
        CategoryResponseDTO responseDTO = CategoryResponseDTO.builder()
                .categoryId(1L)
                .categoryName("Work")
                .build();

        when(categoryService.getAllCategories()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/category-api/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(1L))
                .andExpect(jsonPath("$[0].categoryName").value("Work"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        Long id = 1L;
        String message = "Category with ID 1 has been deleted successfully";

        when(categoryService.deleteCategory(id)).thenReturn(message);

        mockMvc.perform(delete("/category-api/delete/{categoryId}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }
}
