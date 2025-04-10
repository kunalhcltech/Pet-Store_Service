package com.hcltech.service;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO getCategoryById(Long id);
    String deleteCategory(Long id);

}