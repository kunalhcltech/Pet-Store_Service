package com.hcltech.service;

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
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPet_withValidRequest_shouldReturnPetResponse() {
        // Arrange
        PetRequestDTO request = new PetRequestDTO();
        request.setPetName("Buddy");
        request.setAge(2);
        request.setBreed("Labrador");
        request.setGender("Male");
        request.setPrice(1000.0);
        request.setCategoryId(1L);
        request.setTagId(Set.of(10L));

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Dogs");
        TagResponseDTO tagDTO = new TagResponseDTO(10L, "Friendly");

        when(categoryServiceImpl.getCategoryById(1L)).thenReturn(categoryDTO);
        when(tagServiceImpl.getTagById(10L)).thenReturn(tagDTO);

        Tag tag = Tag.builder().tagId(10L).tagName("Friendly").build();
        Category category = Category.builder().categoryId(1L).categoryName("Dogs").build();

        Pet savedPet = Pet.builder()
                .petId(101L)
                .petName("Buddy")
                .age(2)
                .breed("Labrador")
                .gender("Male")
                .price(1000.0)
                .category(category)
                .tags(Set.of(tag))
                .available(true)
                .build();

        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);
        when(tagServiceImpl.mapToResponseDTO(any())).thenReturn(tagDTO);
        when(categoryServiceImpl.mapToResponseDTO(any())).thenReturn(categoryDTO);

        // Act
        PetResponseDTO response = petService.createPet(request);

        // Assert
        assertNotNull(response);
        assertEquals("Buddy", response.getPetName());
        assertEquals("Labrador", response.getBreed());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void getPetById_withValidId_shouldReturnPetResponse() {
        Category category = Category.builder().categoryId(1L).categoryName("Cats").build();
        Pet pet = Pet.builder()
                .petId(1L)
                .petName("Milo")
                .age(3)
                .gender("Male")
                .breed("Persian")
                .price(800.0)
                .category(category)
                .available(true)
                .build();

        CategoryResponseDTO categoryDTO = new CategoryResponseDTO(1L, "Cats");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(categoryServiceImpl.mapToResponseDTO(category)).thenReturn(categoryDTO);

        PetResponseDTO response = petService.getPetById(1L);

        assertNotNull(response);
        assertEquals("Milo", response.getPetName());
    }

    @Test
    void getPetById_withInvalidId_shouldThrowException() {
        assertThrows(InvalidOperationExcepetion.class, () -> petService.getPetById(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> petService.getPetById(null));
    }

    @Test
    void getPetById_petNotFound_shouldThrowException() {
        when(petRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> petService.getPetById(100L));
    }
}
