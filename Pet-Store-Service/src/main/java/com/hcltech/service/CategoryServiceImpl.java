//package com.hcltech.service;
//
//import com.hcltech.dto.CategoryRequestDTO;
//import com.hcltech.dto.CategoryResponseDTO;
//import com.hcltech.exceptions.CetegoryNotFoundException;
//import com.hcltech.model.Category;
//import com.hcltech.repository.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CategoryServiceImpl implements CategoryService {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Override
//    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
//        Category category = Category.builder()
//                .categoryName(categoryRequestDTO.getCategoryName())
//                .build();
//        return mapToResponseDTO(categoryRepository.save(category));
//    }
//
//
//    @Override
//    public List<CategoryResponseDTO> getAllCategories() {
//
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream()
//                .map(category -> CategoryResponseDTO.builder()
//                .categoryId(category.getCategoryId())
//                .categoryName(category.getCategoryName()).build()).toList();
//    }
//
//    @Override
//    public CategoryResponseDTO getCategoryById(Long id) {
//        Category category = categoryRepository.findById(id).orElseThrow(() -> new CetegoryNotFoundException("Category is not found"));
//        return mapToResponseDTO(category);
//    }
//
//    @Override
//    public String deleteCategory(Long id) {
//        Category category = categoryRepository.findById(id).orElseThrow(() -> new CetegoryNotFoundException("Category is not found"));
//        categoryRepository.delete(category);
//        return "Category has been deleted successfully";
//    }
//
//
//    public CategoryResponseDTO mapToResponseDTO(Category category) {
//        return CategoryResponseDTO.builder()
//                .categoryId(category.getCategoryId())
//                .categoryName(category.getCategoryName())
//                .build();
//    }
//}
package com.hcltech.service;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.model.Category;
import com.hcltech.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO == null) {
            throw new InvalidOperationExcepetion("Category request cannot be null.");
        }
        Category category = Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .build();
        return mapToResponseDTO(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid category ID provided.");
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        return mapToResponseDTO(category);
    }

    @Override
    public String deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid category ID provided for deletion.");
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        categoryRepository.delete(category);
        return "Category with ID " + id + " has been deleted successfully";
    }

    public CategoryResponseDTO mapToResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
