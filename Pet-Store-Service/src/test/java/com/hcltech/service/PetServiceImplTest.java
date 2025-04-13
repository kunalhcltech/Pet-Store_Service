package com.hcltech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.PetNotFoundException;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.PetRepository;
import com.hcltech.service.CategoryServiceImpl;
import com.hcltech.service.PetServiceImpl;
import com.hcltech.service.TagServiceImpl;

class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private CategoryServiceImpl categoryService;

    @Mock
    private TagServiceImpl tagService;

    @InjectMocks
    private PetServiceImpl petService;

    private PetRequestDTO petRequestDTO;
    private Pet pet;
    private Category category;
    private CategoryResponseDTO categoryResponseDTO;
    private Tag tag;
    private TagResponseDTO tagResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        petRequestDTO = new PetRequestDTO();
        petRequestDTO.setPetName("Buddy");
        petRequestDTO.setAge(2);
        petRequestDTO.setBreed("Labrador");
        petRequestDTO.setGender("Male");
        petRequestDTO.setPrice(200.0);
        petRequestDTO.setCategoryId(1L);
        petRequestDTO.setTagId(Set.of(1L));

        category = Category.builder().categoryId(1L).categoryName("Dogs").build();
        categoryResponseDTO = CategoryResponseDTO.builder().categoryId(1L).categoryName("Dogs").build();

        tag = Tag.builder().tagId(1L).tagName("Cute").build();
        tagResponseDTO = TagResponseDTO.builder().tagId(1L).tagName("Cute").build();

        pet = Pet.builder()
                .petId(1L)
                .petName("Buddy")
                .age(2)
                .gender("Male")
                .breed("Labrador")
                .price(200.0)
                .available(true)
                .category(category)
                .tags(Set.of(tag))
                .build();
    }

    @Test
    void testCreatePetWithTags() {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponseDTO);
        when(tagService.getTagById(1L)).thenReturn(tagResponseDTO);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);

        PetResponseDTO response = petService.createPet(petRequestDTO);

        assertNotNull(response);
        assertEquals("Buddy", response.getPetName());
    }

    @Test
    void testCreatePetWithoutTags() {
        petRequestDTO.setTagId(null);
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponseDTO);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);

        PetResponseDTO response = petService.createPet(petRequestDTO);

        assertNotNull(response);
        assertEquals("Buddy", response.getPetName());
    }

    @Test
    void testCreatePetInvalidRequest() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.createPet(null));
    }

    @Test
    void testGetAllPets() {
        when(petRepository.findByAvailableTrue()).thenReturn(List.of(pet));
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);

        List<PetResponseDTO> pets = petService.getAllPets();

        assertFalse(pets.isEmpty());
        assertEquals(1, pets.size());
    }

    @Test
    void testGetPetById_Success() {
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.of(pet));
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);

        PetResponseDTO response = petService.getPetById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getPetId());
    }

    @Test
    void testGetPetById_NotFound() {
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> petService.getPetById(1L));
    }

    @Test
    void testDeletePet_Success() {
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.of(pet));

        String result = petService.deletePet(1L);

        assertEquals("Pet with ID 1 has been removed.", result);
        verify(petRepository, times(1)).delete(pet);
    }

    @Test
    void testDeletePet_InvalidId() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.deletePet(0L));
    }

    @Test
    void testUpdatePetPriceById() {
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);

        PetResponseDTO updated = petService.updatePetPriceById(1L, 300.0);

        assertEquals(1L, updated.getPetId());
    }

    @Test
    void testUpdatePetPrice_InvalidPrice() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.updatePetPriceById(1L, -10.0));
    }

    @Test
    void testGetPetByCategory_WithResults() {
        pet.setCategory(category);
        when(petRepository.findByAvailableTrue()).thenReturn(List.of(pet));
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);

        List<PetResponseDTO> pets = petService.getPetByCategory(1L);

        assertEquals(1, pets.size());
    }

    @Test
    void testGetPetByCategory_EmptyResult() {
        when(petRepository.findByAvailableTrue()).thenReturn(Collections.emptyList());

        List<PetResponseDTO> pets = petService.getPetByCategory(1L);

        assertTrue(pets.isEmpty());
    }

    @Test
    void testUpdatePet_Success() {
        when(petRepository.findByPetIdAndAvailableTrue(1L)).thenReturn(Optional.of(pet));
        when(categoryService.getCategoryById(1L)).thenReturn(categoryResponseDTO);
        when(tagService.getTagById(anyLong())).thenReturn(tagResponseDTO);
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);
        when(tagService.mapToResponseDTO(any())).thenReturn(tagResponseDTO);

        PetResponseDTO response = petService.updatePet(1L, petRequestDTO);

        assertNotNull(response);
    }

    @Test
    void testUpdatePet_InvalidId() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.updatePet(0L, petRequestDTO));
    }

    @Test
    void testUpdatePet_NullRequest() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.updatePet(1L, null));
    }

    @Test
    void testMapToResponseDTO_NullTags() {
        pet.setTags(null);
        when(categoryService.mapToResponseDTO(any())).thenReturn(categoryResponseDTO);

        PetResponseDTO dto = petService.mapToResponseDTO(pet);

        assertNotNull(dto);
    }
}