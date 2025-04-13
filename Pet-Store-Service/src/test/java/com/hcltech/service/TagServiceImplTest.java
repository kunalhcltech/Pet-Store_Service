package com.hcltech.service;
import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.PetRepository;
import com.hcltech.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private PetRepository petRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTag_success() {
        TagRequestDTO requestDTO = TagRequestDTO.builder().tagName("Cute").build();
        Tag savedTag = Tag.builder().tagId(1L).tagName("Cute").build();

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);

        TagResponseDTO response = tagService.createTag(requestDTO);

        assertNotNull(response);
        assertEquals("Cute", response.getTagName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testCreateTag_nullRequest_throwsException() {
        InvalidOperationExcepetion ex = assertThrows(InvalidOperationExcepetion.class, () -> {
            tagService.createTag(null);
        });
        assertEquals("Tag request cannot be null.", ex.getMessage());
    }

    @Test
    void testGetAllTags_success() {
        List<Tag> tags = List.of(
                Tag.builder().tagId(1L).tagName("Cute").build(),
                Tag.builder().tagId(2L).tagName("Wild").build()
        );

        when(tagRepository.findAll()).thenReturn(tags);

        List<TagResponseDTO> result = tagService.getAllTags();

        assertEquals(2, result.size());
        verify(tagRepository).findAll();
    }

    @Test
    void testGetTagById_success() {
        Tag tag = Tag.builder().tagId(1L).tagName("Cute").build();

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        TagResponseDTO result = tagService.getTagById(1L);

        assertEquals("Cute", result.getTagName());
    }

    @Test
    void testGetTagById_nullId_throwsException() {
        InvalidOperationExcepetion ex = assertThrows(InvalidOperationExcepetion.class, () -> {
            tagService.getTagById(null);
        });
        assertEquals("Invalid tag ID provided.", ex.getMessage());
    }

    @Test
    void testGetTagById_invalidId_throwsException() {
        InvalidOperationExcepetion ex = assertThrows(InvalidOperationExcepetion.class, () -> {
            tagService.getTagById(-1L);
        });
        assertEquals("Invalid tag ID provided.", ex.getMessage());
    }

    @Test
    void testGetTagById_tagNotFound_throwsException() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        TagNotFoundException ex = assertThrows(TagNotFoundException.class, () -> {
            tagService.getTagById(1L);
        });
        assertEquals("Tag not found with ID: 1", ex.getMessage());
    }

    @Test
    void testDeleteTag_success() {
        Tag tag = Tag.builder().tagId(1L).tagName("Cute").build();
        Pet pet = Pet.builder().tags(new HashSet<>(List.of(tag))).build();

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(petRepository.findAll()).thenReturn(List.of(pet));

        String result = tagService.deleteTag(1L);

        assertEquals("Tag with ID 1 has been deleted successfully", result);
        verify(tagRepository).delete(tag);
        verify(petRepository).saveAll(anyList());
    }

    @Test
    void testDeleteTag_nullId_throwsException() {
        InvalidOperationExcepetion ex = assertThrows(InvalidOperationExcepetion.class, () -> {
            tagService.deleteTag(null);
        });
        assertEquals("Invalid tag ID provided for deletion.", ex.getMessage());
    }

    @Test
    void testDeleteTag_invalidId_throwsException() {
        InvalidOperationExcepetion ex = assertThrows(InvalidOperationExcepetion.class, () -> {
            tagService.deleteTag(-5L);
        });
        assertEquals("Invalid tag ID provided for deletion.", ex.getMessage());
    }

    @Test
    void testDeleteTag_tagNotFound_throwsException() {
        when(tagRepository.findById(2L)).thenReturn(Optional.empty());

        TagNotFoundException ex = assertThrows(TagNotFoundException.class, () -> {
            tagService.deleteTag(2L);
        });

        assertEquals("Tag not found with ID: 2", ex.getMessage());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}