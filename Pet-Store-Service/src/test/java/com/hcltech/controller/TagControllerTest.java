package com.hcltech.controller;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.service.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TagControllerTest {

    private MockMvc mockMvc;
    private TagServiceImpl tagService;
    private TagController tagController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        tagService = mock(TagServiceImpl.class);
        tagController = new TagController();
        // Manually inject the mock (since you're not using @MockBean)
        tagController.getClass().getDeclaredFields();
        // using reflection to inject service
        try {
            var field = TagController.class.getDeclaredField("tagServiceImpl");
            field.setAccessible(true);
            field.set(tagController, tagService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    void testCreateTag() throws Exception {
        TagRequestDTO request = TagRequestDTO.builder().tagName("TestTag").build();
        TagResponseDTO response = TagResponseDTO.builder().tagId(1L).tagName("TestTag").build();

        when(tagService.createTag(any(TagRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/tag-api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagName").value("TestTag"));
    }

    @Test
    void testGetAllTags() throws Exception {
        TagResponseDTO response = TagResponseDTO.builder().tagId(1L).tagName("Sample").build();
        when(tagService.getAllTags()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/tag-api/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagName").value("Sample"));
    }

    @Test
    void testDeleteTag() throws Exception {
        Long tagId = 1L;
        when(tagService.deleteTag(tagId)).thenReturn("Deleted");

        mockMvc.perform(delete("/tag-api/delete/{id}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }
}
