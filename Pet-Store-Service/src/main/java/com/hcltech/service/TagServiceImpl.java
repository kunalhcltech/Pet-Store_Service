package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Tag;
import com.hcltech.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequestDTO) {
        if (tagRequestDTO == null) {
            throw new InvalidOperationExcepetion("Tag request cannot be null.");
        }
        Tag tag = Tag.builder()
                .tagName(tagRequestDTO.getTagName()).build();
        return mapToResponseDTO(tagRepository.save(tag));
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid tag ID provided.");
        }
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + id));
        return mapToResponseDTO(tag);
    }

    @Override
    public String deleteTag(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid tag ID provided for deletion.");
        }
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + id));
        tagRepository.delete(tag);
        return "Tag with ID " + id + " has been deleted successfully";
    }

    public TagResponseDTO mapToResponseDTO(Tag tag) {
        return TagResponseDTO.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .build();
    }
}