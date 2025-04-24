package com.hcltech.controller;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category-api")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @PostMapping("/create")
    @Operation(summary = "Category Create",description = "This method creates the category")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryServiceImpl.createCategory(categoryRequestDTO));
    }

    @GetMapping("/get")
    @Operation(summary = "Get category",description = "This method retrieves all the categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory()
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoryServiceImpl.getAllCategories());
    }

    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete Category",description = "This mehtod deletes the category")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("categoryId")Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(categoryServiceImpl.deleteCategory(categoryId));
    }

}