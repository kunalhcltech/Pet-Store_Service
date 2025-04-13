package com.hcltech.controller;

import com.hcltech.controller.TagController;
import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.service.TagServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TagControllerTest {

    @InjectMocks
    private TagController tagController;

    @Mock
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTag() {
        // Arrange
        TagRequestDTO request = new TagRequestDTO();
        TagResponseDTO response = new TagResponseDTO();
        when(tagService.createTag(request)).thenReturn(response);

        // Act
        ResponseEntity<TagResponseDTO> result = tagController.createTag(request);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response);
        verify(tagService, times(1)).createTag(request);
    }

    @Test
    void testGetAllTags() {
        // Arrange
        List<TagResponseDTO> tags = List.of(new TagResponseDTO(), new TagResponseDTO());
        when(tagService.getAllTags()).thenReturn(tags);

        // Act
        ResponseEntity<List<TagResponseDTO>> result = tagController.getAllTag();

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).hasSize(2);
        verify(tagService, times(1)).getAllTags();
    }

    @Test
    void testDeleteTagById() {
        // Arrange
        Long id = 1L;
        String expectedMessage = "Tag deleted successfully";
        when(tagService.deleteTag(id)).thenReturn(expectedMessage);

        // Act
        ResponseEntity<String> result = tagController.deleteById(id);

        // Assert
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(expectedMessage);
        verify(tagService, times(1)).deleteTag(id);
    }
    //----------------------------------------------------------------------
    @Test
    void testCreateTagThrowsException() {
        TagRequestDTO request = new TagRequestDTO();
        when(tagService.createTag(request)).thenThrow(new RuntimeException("Something went wrong"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tagController.createTag(request);
        });

        assertThat(ex.getMessage()).isEqualTo("Something went wrong");
        verify(tagService, times(1)).createTag(request);
    }

    @Test
    void testGetAllTagsThrowsException() {
        when(tagService.getAllTags()).thenThrow(new RuntimeException("Failed to fetch tags"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tagController.getAllTag();
        });

        assertThat(ex.getMessage()).isEqualTo("Failed to fetch tags");
        verify(tagService, times(1)).getAllTags();
    }

    @Test
    void testDeleteTagByIdThrowsTagNotFoundException() {
        Long tagId = 99L;
        when(tagService.deleteTag(tagId)).thenThrow(new TagNotFoundException("Tag not found"));

        TagNotFoundException ex = assertThrows(TagNotFoundException.class, () -> {
            tagController.deleteById(tagId);
        });

        assertThat(ex.getMessage()).isEqualTo("Tag not found");
        verify(tagService, times(1)).deleteTag(tagId);
    }


}