package com.hcltech.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.repository.CategoryRepository;
import com.hcltech.repository.PetRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        logger.info("Creating category with request: ", categoryRequestDTO);
        if (categoryRequestDTO == null) {
            logger.error("Category request is null");
            throw new InvalidOperationExcepetion("Category request cannot be null.");
        }
        Category category = Category.builder()
                .categoryName(categoryRequestDTO.getCategoryName())
                .build();
        Category savedCategory = categoryRepository.save(category);
        logger.info("Category created with ID: ", savedCategory.getCategoryId());
        return mapToResponseDTO(savedCategory);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        logger.info("Fetching category by ID: ", id);
        if (id == null || id <= 0) {
            logger.error("Invalid category ID: ", id);
            throw new InvalidOperationExcepetion("Invalid category ID provided.");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: ", id);
                    return new CategoryNotFoundException("Category not found with ID: " + id);
                });
        return mapToResponseDTO(category);
    }

    @Override
    public String deleteCategory(Long id) {
        logger.info("Deleting category with ID: ", id);
        if (id == null || id <= 0) {
            logger.error("Invalid category ID for deletion: ", id);
            throw new InvalidOperationExcepetion("Invalid category ID provided for deletion.");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: ", id);
                    return new CategoryNotFoundException("Category not found with ID: " + id);
                });

        List<Pet> pets = petRepository.findByCategory(category);
        logger.info("Disassociating  pets from category ID: ", pets.size(), id);

        for (Pet pet : pets) {
            pet.setCategory(null);
        }
        petRepository.saveAll(pets);

        categoryRepository.delete(category);
        logger.info("Category with ID  deleted successfully", id);
        return "Category with ID " + id + " has been deleted successfully";
    }

    public CategoryResponseDTO mapToResponseDTO(Category category) {
        if (category == null) {
            return null;
        }
        return mapper.map(category, CategoryResponseDTO.class);
    }
}
