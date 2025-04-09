package com.hcltech.controller;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-api")

public class CategoryController {
@PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO)
    {
        return null;
    }

    @GetMapping("/get")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategory()
    {
        return null;
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("categoryId")Integer categoryId){
        return null;
    }
}
