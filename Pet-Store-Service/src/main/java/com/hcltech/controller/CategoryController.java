package com.hcltech.controller;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category-api")

public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryServiceImpl.createCategory(categoryRequestDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory()
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoryServiceImpl.getAllCategories());
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("categoryId")Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(categoryServiceImpl.deleteCategory(categoryId));
    }
}