package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Tag;
import com.hcltech.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void createTag_ValidInput_ReturnsResponseDTO() {
        // Arrange
        TagRequestDTO requestDTO = TagRequestDTO.builder().tagName("Technology").build();
        Tag savedTag = Tag.builder().tagId(201L).tagName("Technology").build();

        Mockito.when(tagRepository.save(Mockito.any(Tag.class))).thenReturn(savedTag);

        // Act
        TagResponseDTO responseDTO = tagService.createTag(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(savedTag.getTagId(), responseDTO.getTagId());
        assertEquals(savedTag.getTagName(), responseDTO.getTagName());
        Mockito.verify(tagRepository, Mockito.times(1)).save(Mockito.any(Tag.class));
    }

    @Test
    void createTag_NullInput_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.createTag(null));
        Mockito.verify(tagRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getAllTags_ReturnsListOfResponseDTOs() {
        // Arrange
        List<Tag> tags = Arrays.asList(
                Tag.builder().tagId(201L).tagName("Technology").build(),
                Tag.builder().tagId(202L).tagName("Gaming").build()
        );
        Mockito.when(tagRepository.findAll()).thenReturn(tags);

        // Act
        List<TagResponseDTO> responseDTOs = tagService.getAllTags();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(2, responseDTOs.size());
        assertEquals(tags.get(0).getTagId(), responseDTOs.get(0).getTagId());
        assertEquals(tags.get(0).getTagName(), responseDTOs.get(0).getTagName());
        assertEquals(tags.get(1).getTagId(), responseDTOs.get(1).getTagId());
        assertEquals(tags.get(1).getTagName(), responseDTOs.get(1).getTagName());
        Mockito.verify(tagRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getTagById_ExistingId_ReturnsResponseDTO() {
        // Arrange
        Long tagId = 201L;
        Tag tag = Tag.builder().tagId(tagId).tagName("Technology").build();
        Mockito.when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        // Act
        TagResponseDTO responseDTO = tagService.getTagById(tagId);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(tag.getTagId(), responseDTO.getTagId());
        assertEquals(tag.getTagName(), responseDTO.getTagName());
        Mockito.verify(tagRepository, Mockito.times(1)).findById(tagId);
    }

    @Test
    void getTagById_NonExistingId_ThrowsTagNotFoundException() {
        // Arrange
        Long tagId = 999L;
        Mockito.when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(tagId));
        Mockito.verify(tagRepository, Mockito.times(1)).findById(tagId);
    }

    @Test
    void getTagById_NullId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.getTagById(null));
        Mockito.verify(tagRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void getTagById_InvalidId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.getTagById(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.getTagById(-1L));
        Mockito.verify(tagRepository, Mockito.never()).findById(Mockito.any());
    }

    @Test
    void deleteTag_ExistingId_ReturnsSuccessMessage() {
        // Arrange
        Long tagIdToDelete = 201L;
        Tag tagToDelete = Tag.builder().tagId(tagIdToDelete).tagName("Technology").build();
        Mockito.when(tagRepository.findById(tagIdToDelete)).thenReturn(Optional.of(tagToDelete));
        Mockito.doNothing().when(tagRepository).delete(tagToDelete);

        // Act
        String result = tagService.deleteTag(tagIdToDelete);

        // Assert
        assertEquals("Tag with ID " + tagIdToDelete + " has been deleted successfully", result);
        Mockito.verify(tagRepository, Mockito.times(1)).findById(tagIdToDelete);
        Mockito.verify(tagRepository, Mockito.times(1)).delete(tagToDelete);
    }

    @Test
    void deleteTag_NonExistingId_ThrowsTagNotFoundException() {
        // Arrange
        Long nonExistingTagId = 999L;
        Mockito.when(tagRepository.findById(nonExistingTagId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(nonExistingTagId));
        Mockito.verify(tagRepository, Mockito.times(1)).findById(nonExistingTagId);
        Mockito.verify(tagRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteTag_NullId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.deleteTag(null));
        Mockito.verify(tagRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(tagRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteTag_InvalidId_ThrowsInvalidOperationException() {
        // Act & Assert
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.deleteTag(0L));
        assertThrows(InvalidOperationExcepetion.class, () -> tagService.deleteTag(-1L));
        Mockito.verify(tagRepository, Mockito.never()).findById(Mockito.any());
        Mockito.verify(tagRepository, Mockito.never()).delete(Mockito.any());
    }
}
