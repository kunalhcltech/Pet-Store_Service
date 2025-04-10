package com.hcltech.service;

import com.hcltech.dto.*;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.PetNotFoundException;
import com.hcltech.model.*;
import com.hcltech.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private TagServiceImpl tagServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePet_WithTags() {
        PetRequestDTO request = getSampleRequest(true);
        when(categoryServiceImpl.getCategoryById(1L)).thenReturn(getCategoryResponse());
        when(tagServiceImpl.getTagById(1L)).thenReturn(getTagResponse());
        when(petRepository.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        when(categoryServiceImpl.mapToResponseDTO(any(Category.class))).thenReturn(getCategoryResponse());
        when(tagServiceImpl.mapToResponseDTO(any(Tag.class))).thenReturn(getTagResponse());

        PetResponseDTO result = petService.createPet(request);
        assertEquals("Dog", result.getPetName());
    }

    @Test
    void testGetAllPets() {
        List<Pet> pets = List.of(getSamplePet());
        when(petRepository.findAll()).thenReturn(pets);
        when(categoryServiceImpl.mapToResponseDTO(any(Category.class))).thenReturn(getCategoryResponse());

        PetResponseDTO responseDTO = PetResponseDTO.builder().petName("Dog").build();
        when(tagServiceImpl.mapToResponseDTO(any(Tag.class))).thenReturn(getTagResponse());

        List<PetResponseDTO> result = petService.getAllPets();
        assertEquals(1, result.size());
    }

    @Test
    void testGetPetById_ValidId() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(getSamplePet()));
        when(categoryServiceImpl.mapToResponseDTO(any(Category.class))).thenReturn(getCategoryResponse());
        when(tagServiceImpl.mapToResponseDTO(any(Tag.class))).thenReturn(getTagResponse());

        PetResponseDTO result = petService.getPetById(1L);
        assertEquals("Dog", result.getPetName());
    }

    @Test
    void testGetPetById_InvalidId() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.getPetById(0L));
    }

    @Test
    void testUpdatePet() {
        Pet pet = getSamplePet();
        PetRequestDTO request = getSampleRequest(true);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(categoryServiceImpl.getCategoryById(1L)).thenReturn(getCategoryResponse());
        when(tagServiceImpl.getTagById(1L)).thenReturn(getTagResponse());
        when(petRepository.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        when(categoryServiceImpl.mapToResponseDTO(any(Category.class))).thenReturn(getCategoryResponse());
        when(tagServiceImpl.mapToResponseDTO(any(Tag.class))).thenReturn(getTagResponse());

        PetResponseDTO updatedPet = petService.updatePet(1L, request);
        assertEquals("Dog", updatedPet.getPetName());
    }

    @Test
    void testDeletePet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(getSamplePet()));

        String message = petService.deletePet(1L);
        verify(petRepository).delete(any(Pet.class));
        assertTrue(message.contains("has been removed"));
    }

    @Test
    void testGetPetByCategory() {
        Pet pet = getSamplePet();
        pet.setCategory(Category.builder().categoryId(1L).categoryName("Domestic").build());

        when(petRepository.findAll()).thenReturn(List.of(pet));
        when(categoryServiceImpl.mapToResponseDTO(any())).thenReturn(getCategoryResponse());
        when(tagServiceImpl.mapToResponseDTO(any())).thenReturn(getTagResponse());

        List<PetResponseDTO> result = petService.getPetByCategory(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdatePetPriceById() {
        Pet pet = getSamplePet();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        when(categoryServiceImpl.mapToResponseDTO(any(Category.class))).thenReturn(getCategoryResponse());
        when(tagServiceImpl.mapToResponseDTO(any(Tag.class))).thenReturn(getTagResponse());

        PetResponseDTO result = petService.updatePetPriceById(1L, 150.0);
        assertEquals(150.0, result.getPrice());
    }

    // ------------------ Helpers ------------------

    private Pet getSamplePet() {
        return Pet.builder()
                .petId(1L)
                .petName("Dog")
                .price(100.0)
                .available(true)
                .category(Category.builder().categoryId(1L).categoryName("Domestic").build())
                .tags(Set.of(Tag.builder().tagId(1L).tagName("Friendly").build()))
                .build();
    }

    private PetRequestDTO getSampleRequest(boolean withTags) {
        return PetRequestDTO.builder()
                .petName("Dog")
                .age(2)
                .price(100.0)
                .gender("Male")
                .breed("Labrador")
                .categoryId(1L)
                .tagId(withTags ? Set.of(1L) : null)
                .build();
    }

    private CategoryResponseDTO getCategoryResponse() {
        return CategoryResponseDTO.builder()
                .categoryId(1L)
                .categoryName("Domestic")
                .build();
    }

    private TagResponseDTO getTagResponse() {
        return TagResponseDTO.builder()
                .tagId(1L)
                .tagName("Friendly")
                .build();
    }
}
