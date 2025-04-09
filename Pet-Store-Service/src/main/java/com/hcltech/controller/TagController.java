package com.hcltech.controller;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag-api")
public class TagController {
    @PostMapping("/create")
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO){
        return null;
    }
    @GetMapping("/get")
    public ResponseEntity<TagResponseDTO> getAllTag(){
        return null;
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable ("id") Integer id)
    {
        return null;
    }
}
