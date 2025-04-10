package com.hcltech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.service.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(TagController.class)
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagServiceImpl tagService;

    @Test
    void createTag_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        TagRequestDTO requestDTO = TagRequestDTO.builder().tagName("Technology").build();
        TagResponseDTO responseDTO = TagResponseDTO.builder().tagId(201L).tagName("Technology").build();

        Mockito.when(tagService.createTag(requestDTO)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/tag-api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagId").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tagName").value("Technology"));

        Mockito.verify(tagService, Mockito.times(1)).createTag(requestDTO);
    }

    @Test
    void getAllTag_ReturnsListOfTags() throws Exception {
        // Arrange
        List<TagResponseDTO> responseList = Arrays.asList(
                TagResponseDTO.builder().tagId(201L).tagName("Technology").build(),
                TagResponseDTO.builder().tagId(202L).tagName("Gaming").build()
        );

        Mockito.when(tagService.getAllTags()).thenReturn(responseList);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/tag-api/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tagId").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tagName").value("Technology"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tagId").value(202))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].tagName").value("Gaming"));

        Mockito.verify(tagService, Mockito.times(1)).getAllTags();
    }

    @Test
    void deleteById_ExistingId_ReturnsOkWithMessage() throws Exception {
        // Arrange
        Long tagIdToDelete = 201L;
        String deletionMessage = "Tag with ID " + tagIdToDelete + " has been deleted successfully";

        Mockito.when(tagService.deleteTag(tagIdToDelete)).thenReturn(deletionMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/tag-api/delete/{id}", tagIdToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(deletionMessage));

        Mockito.verify(tagService, Mockito.times(1)).deleteTag(tagIdToDelete);
    }

    @Test
    void deleteById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Long nonExistingTagId = 999L;
        String notFoundMessage = "Tag not found with ID: " + nonExistingTagId;

        Mockito.when(tagService.deleteTag(nonExistingTagId)).thenThrow(new com.hcltech.exceptions.TagNotFoundException(notFoundMessage));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/tag-api/delete/{id}", nonExistingTagId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(notFoundMessage));

        Mockito.verify(tagService, Mockito.times(1)).deleteTag(nonExistingTagId);
    }
}
