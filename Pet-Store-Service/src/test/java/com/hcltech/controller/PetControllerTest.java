package com.hcltech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.service.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PetServiceImpl petServiceImpl;

    @InjectMocks
    private PetController petController;

    private ObjectMapper objectMapper;

    private PetResponseDTO petResponse;
    private PetRequestDTO petRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        TagResponseDTO tag1 = TagResponseDTO.builder().tagName("Vacinated").tagId(3L).build();
        TagResponseDTO tag2 = TagResponseDTO.builder().tagName("Friendly").tagId(4L).build();

        Set<TagResponseDTO> tagSet = new HashSet<>();
        tagSet.add(tag1);
        tagSet.add(tag2);

        petResponse = PetResponseDTO.builder()
                .petId(1L)
                .petName("Charlie")
                .price(500.0)
                .category(CategoryResponseDTO.builder().categoryName("Dog").categoryId(1L).build())
                .tags(tagSet)
                .build();

        Set<Long> sets=Set.of(3L,4L);

        petRequest = PetRequestDTO.builder()
                .petName("Charlie")
                .price(500.0)
                .categoryId(1L)
                .tagId(sets)
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(petController).build();
    }

    @Test
    void testCreatePet() throws Exception {
        when(petServiceImpl.createPet(any(PetRequestDTO.class))).thenReturn(petResponse);

        mockMvc.perform(post("/pet-api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petName").value("Charlie"))
                .andExpect(jsonPath("$.price").value(500.0));

        verify(petServiceImpl, times(1)).createPet(any(PetRequestDTO.class));
    }

    @Test
    void testUpdatePrice() throws Exception {
        when(petServiceImpl.updatePetPriceById(1L, 750.0)).thenReturn(petResponse.toBuilder().price(750.0).build());

        mockMvc.perform(put("/pet-api/update-price")
                        .param("id", "1")
                        .param("price", "750.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(750.0));
    }

    @Test
    void testDeletePet() throws Exception {
        when(petServiceImpl.deletePet(1L)).thenReturn("Deleted Successfully");

        mockMvc.perform(delete("/pet-api/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted Successfully"));
    }

    @Test
    void testGetPetByCategory() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponse);
        when(petServiceImpl.getPetByCategory(2L)).thenReturn(pets);

        mockMvc.perform(get("/pet-api/getByCategory/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category.categoryId").value(1L));
    }

    @Test
    void testGetPetById() throws Exception {
        when(petServiceImpl.getPetById(1L)).thenReturn(petResponse);

        mockMvc.perform(get("/pet-api/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.petId").value(1L));
    }

    @Test
    void testGetAllPets() throws Exception {
        List<PetResponseDTO> pets = Arrays.asList(petResponse);
        when(petServiceImpl.getAllPets()).thenReturn(pets);

        mockMvc.perform(get("/pet-api/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].petId").value(1L));
    }
}