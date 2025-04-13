package com.hcltech.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.hcltech.controller.PetController;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.service.PetServiceImpl;

class PetControllerTest {

    @Mock
    private PetServiceImpl petServiceImpl;

    @InjectMocks
    private PetController petController;

    private PetRequestDTO petRequestDTO;
    private PetResponseDTO petResponseDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        petRequestDTO = new PetRequestDTO();
        // Initialize your fields if needed

        petResponseDTO = new PetResponseDTO();
        petResponseDTO.setPetId(1L);
        petResponseDTO.setPetName("Bobby");
        petResponseDTO.setPrice(500.0);
    }

    @Test
    void testCreatePet() {
        when(petServiceImpl.createPet(any(PetRequestDTO.class))).thenReturn(petResponseDTO);

        ResponseEntity<PetResponseDTO> response = petController.createPet(petRequestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bobby", response.getBody().getPetName());
    }

    @Test
    void testUpdatePetPriceById() {
        when(petServiceImpl.updatePetPriceById(1L, 800.0)).thenReturn(petResponseDTO);

        ResponseEntity<PetResponseDTO> response = petController.updatePetPriceById(1L, 800.0);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getPetId());
    }

    @Test
    void testDeletePetById() {
        when(petServiceImpl.deletePet(1L)).thenReturn("Deleted Successfully");

        ResponseEntity<String> response = petController.deletePetById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted Successfully", response.getBody());
    }

    @Test
    void testGetPetByCategory() {
        List<PetResponseDTO> list = Arrays.asList(petResponseDTO);
        when(petServiceImpl.getPetByCategory(1L)).thenReturn(list);

        ResponseEntity<List<PetResponseDTO>> response = petController.getPetByCategory(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetPetByCategory_Empty() {
        when(petServiceImpl.getPetByCategory(2L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PetResponseDTO>> response = petController.getPetByCategory(2L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetById() {
        when(petServiceImpl.getPetById(1L)).thenReturn(petResponseDTO);

        ResponseEntity<PetResponseDTO> response = petController.getById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bobby", response.getBody().getPetName());
    }

    @Test
    void testGetAllPets() {
        when(petServiceImpl.getAllPets()).thenReturn(Arrays.asList(petResponseDTO));

        ResponseEntity<List<PetResponseDTO>> response = petController.getAllPets();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllPets_Empty() {
        when(petServiceImpl.getAllPets()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PetResponseDTO>> response = petController.getAllPets();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }
}