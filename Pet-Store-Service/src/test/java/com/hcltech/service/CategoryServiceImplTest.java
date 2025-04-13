package com.hcltech.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.hcltech.dto.CategoryRequestDTO;
import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.exceptions.CategoryNotFoundException;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.repository.CategoryRepository;
import com.hcltech.repository.PetRepository;
import com.hcltech.service.CategoryServiceImpl;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. createCategory - success
    @Test
    void testCreateCategorySuccess() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setCategoryName("Dogs");

        Category savedCategory = Category.builder().categoryId(1L).categoryName("Dogs").build();
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setCategoryId(1L);
        responseDTO.setCategoryName("Dogs");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(modelMapper.map(savedCategory, CategoryResponseDTO.class)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.createCategory(requestDTO);

        assertEquals(1L, result.getCategoryId());
        assertEquals("Dogs", result.getCategoryName());
    }

    // 2. createCategory - null input
    @Test
    void testCreateCategoryNullInput() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.createCategory(null));
    }

    // 3. getAllCategories - success
    @Test
    void testGetAllCategories() {
        Category category = Category.builder().categoryId(1L).categoryName("Cats").build();
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(1L);
        dto.setCategoryName("Cats");

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(modelMapper.map(category, CategoryResponseDTO.class)).thenReturn(dto);

        List<CategoryResponseDTO> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Cats", result.get(0).getCategoryName());
    }

    // 4. getCategoryById - success
    @Test
    void testGetCategoryByIdSuccess() {
        Category category = Category.builder().categoryId(2L).categoryName("Fish").build();
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(2L);
        dto.setCategoryName("Fish");

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryResponseDTO.class)).thenReturn(dto);

        CategoryResponseDTO result = categoryService.getCategoryById(2L);

        assertEquals("Fish", result.getCategoryName());
    }

    // 5. getCategoryById - null ID
    @Test
    void testGetCategoryByIdNull() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(null));
    }

    // 6. getCategoryById - invalid ID
    @Test
    void testGetCategoryByIdInvalid() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.getCategoryById(-1L));
    }

    // 7. getCategoryById - not found
    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    // 8. deleteCategory - success
    @Test
    void testDeleteCategorySuccess() {
        Long categoryId = 3L;
        Category category = Category.builder().categoryId(categoryId).categoryName("Birds").build();
        Pet pet1 = new Pet();
        pet1.setCategory(category);

        Pet pet2 = new Pet();
        pet2.setCategory(category);

        List<Pet> pets = List.of(pet1, pet2);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(petRepository.findByCategory(category)).thenReturn(pets);

        String result = categoryService.deleteCategory(categoryId);

        verify(petRepository).saveAll(anyList());
        verify(categoryRepository).delete(category);

        assertEquals("Category with ID 3 has been deleted successfully", result);
    }

    // 9. deleteCategory - null ID
    @Test
    void testDeleteCategoryWithNullId() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(null));
    }

    // 10. deleteCategory - invalid ID
    @Test
    void testDeleteCategoryWithInvalidId() {
        assertThrows(InvalidOperationExcepetion.class, () -> categoryService.deleteCategory(0L));
    }

    // 11. deleteCategory - not found
    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(100L));
    }

    // 12. mapToResponseDTO - null input
    @Test
    void testMapToResponseDTONull() {
        assertNull(categoryService.mapToResponseDTO(null));
    }
}