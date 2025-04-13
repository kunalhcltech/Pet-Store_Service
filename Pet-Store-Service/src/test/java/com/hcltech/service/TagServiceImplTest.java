package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.PetRepository;
import com.hcltech.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Logger logger; // Mocking the logger

    @InjectMocks
    private TagServiceImpl tagService;

    private TagRequestDTO tagRequestDTO;
    private Tag tag;
    private TagResponseDTO tagResponseDTO;
    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tagRequestDTO = new TagRequestDTO();
        tagRequestDTO.setTagName("Playful");

        tag = Tag.builder()
                .tagId(1L)
                .tagName("Playful")
                .build();

        tagResponseDTO = TagResponseDTO.builder()
                .tagId(1L)
                .tagName("Playful")
                .build();

        pet1 = new Pet();
        pet1.setPetId(101L);
        pet1.setPetName("Buddy");
        pet1.setTags(new HashSet<>(Arrays.asList(tag)));

        pet2 = new Pet();
        pet2.setPetId(102L);
        pet2.setPetName("Lucy");
        pet2.setTags(new HashSet<>(Arrays.asList(tag)));
    }

    @Test
    void createTag_ValidRequest_ReturnsResponseDTO() {
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        when(tagService.mapToResponseDTO(tag)).thenReturn(tagResponseDTO);

        TagResponseDTO result = tagService.createTag(tagRequestDTO);

        assertNotNull(result);
        assertEquals(tagResponseDTO.getTagId(), result.getTagId());
        assertEquals(tagResponseDTO.getTagName(), result.getTagName());
        verify(tagRepository, times(1)).save(any(Tag.class));
        verify(tagService, times(1)).mapToResponseDTO(tag);
    }

    @Test
    void createTag_NullRequest_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.createTag(null));
        verify(tagRepository, never()).save(any());
        verify(tagService, never()).mapToResponseDTO(any());
    }

    @Test
    void getAllTags_ReturnsListOfResponseDTOs() {
        List<Tag> tags = Arrays.asList(tag);
        List<TagResponseDTO> responseList = Arrays.asList(tagResponseDTO);

        when(tagRepository.findAll()).thenReturn(tags);
        when(tagService.mapToResponseDTO(tag)).thenReturn(tagResponseDTO);

        List<TagResponseDTO> result = tagService.getAllTags();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tagResponseDTO.getTagId(), result.get(0).getTagId());
        assertEquals(tagResponseDTO.getTagName(), result.get(0).getTagName());
        verify(tagRepository, times(1)).findAll();
        verify(tagService, times(1)).mapToResponseDTO(tag);
    }

    @Test
    void getAllTags_NoTags_ReturnsEmptyList() {
        when(tagRepository.findAll()).thenReturn(Arrays.asList());

        List<TagResponseDTO> result = tagService.getAllTags();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(tagRepository, times(1)).findAll();
        verify(tagService, never()).mapToResponseDTO(any());
    }

    @Test
    void getTagById_ValidId_ReturnsResponseDTO() {
        Long tagId = 1L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        when(tagService.mapToResponseDTO(tag)).thenReturn(tagResponseDTO);

        TagResponseDTO result = tagService.getTagById(tagId);

        assertNotNull(result);
        assertEquals(tagResponseDTO.getTagId(), result.getTagId());
        assertEquals(tagResponseDTO.getTagName(), result.getTagName());
        verify(tagRepository, times(1)).findById(tagId);
        verify(tagService, times(1)).mapToResponseDTO(tag);
    }

    @Test
    void getTagById_InvalidId_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.getTagById(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.getTagById(null));
        verify(tagRepository, never()).findById(any());
        verify(tagService, never()).mapToResponseDTO(any());
    }

    @Test
    void getTagById_NotFound_ThrowsTagNotFoundException() {
        Long tagId = 99L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(tagId));
        verify(tagRepository, times(1)).findById(tagId);
        verify(tagService, never()).mapToResponseDTO(any());
    }

    @Test
    void deleteTag_ValidId_DeletesTagAndRemovesFromPets() {
        Long tagId = 1L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));
        when(petRepository.findAll()).thenReturn(Arrays.asList(pet1, pet2));
        when(petRepository.saveAll(anyList())).thenReturn(Arrays.asList(pet1, pet2));

        String result = tagService.deleteTag(tagId);

        assertEquals("Tag with ID " + tagId + " has been deleted successfully", result);
        assertFalse(pet1.getTags().contains(tag));
        assertFalse(pet2.getTags().contains(tag));
        verify(tagRepository, times(1)).findById(tagId);
        verify(petRepository, times(1)).findAll();
        verify(petRepository, times(1)).saveAll(anyList());
        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    void deleteTag_InvalidId_ThrowsInvalidOperationException() {
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.deleteTag(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.deleteTag(null));
        verify(tagRepository, never()).findById(any());
        verify(petRepository, never()).findAll();
        verify(petRepository, never()).saveAll(anyList());
        verify(tagRepository, never()).delete(any());
    }

    @Test
    void deleteTag_NotFound_ThrowsTagNotFoundException() {
        Long tagId = 99L;
        when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(tagId));
        verify(tagRepository, times(1)).findById(tagId);
        verify(petRepository, never()).findAll();
        verify(petRepository, never()).saveAll(anyList());
        verify(tagRepository, never()).delete(any());
    }

    @Test
    void mapToResponseDTO_ValidTag_ReturnsResponseDTO() {
        TagResponseDTO result = tagService.mapToResponseDTO(tag);
        assertNotNull(result);
        assertEquals(tag.getTagId(), result.getTagId());
        assertEquals(tag.getTagName(), result.getTagName());
    }
}