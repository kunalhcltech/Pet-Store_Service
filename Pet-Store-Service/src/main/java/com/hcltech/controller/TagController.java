package com.hcltech.controller;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.service.TagServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag-api")
public class TagController {

    @Autowired
    private TagServiceImpl tagServiceImpl;
    @PostMapping("/create")
    @Operation(summary = "Create Tag",description = "This method will create Tag")
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceImpl.createTag(tagRequestDTO));
    }
    @GetMapping("/get")
    @Operation(summary = "Get Tag",description = "This method will gett all tag's")
    public ResponseEntity<List<TagResponseDTO>> getAllTag(){
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceImpl.getAllTags());

    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Tag",description = "This method will delete the tag")
    public ResponseEntity<String> deleteById(@PathVariable ("id") Long id)
    {
        return ResponseEntity.status(HttpStatus.OK).body(tagServiceImpl.deleteTag(id));
    }

}