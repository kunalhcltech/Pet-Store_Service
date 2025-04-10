package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Tag;
import com.hcltech.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTag() {
        TagRequestDTO requestDTO = new TagRequestDTO("Important");
        Tag tag = Tag.builder().tagId(1L).tagName("Important").build();

        when(tagRepository.save(any())).thenReturn(tag);

        TagResponseDTO responseDTO = tagService.createTag(requestDTO);

        assertEquals("Important", responseDTO.getTagName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void testGetAllTags() {
        List<Tag> tags = Arrays.asList(
                Tag.builder().tagId(1L).tagName("A").build(),
                Tag.builder().tagId(2L).tagName("B").build()
        );

        when(tagRepository.findAll()).thenReturn(tags);

        List<TagResponseDTO> result = tagService.getAllTags();
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteTag_TagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(1L));
    }
}
