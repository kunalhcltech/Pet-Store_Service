package com.hcltech.service;

import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.TagNoFoundException;
import com.hcltech.model.Category;
import com.hcltech.model.Tag;
import com.hcltech.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequestDTO) {
        Tag tag = Tag.builder()
                .tagName(tagRequestDTO.getTagName()).build();
        return mapToResponseDTO(tagRepository.save(tag));
    }

    @Override
    public List<TagResponseDTO> getAllTags() {

        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> TagResponseDTO.builder()
                        .tagId(tag.getTagId())
                        .tagName(tag.getTagName()).build())
                .toList();
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNoFoundException("Tag not found"));
        return mapToResponseDTO(tag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNoFoundException("Tag not found"));
        tagRepository.delete(tag);
    }

    public TagResponseDTO mapToResponseDTO(Tag tag) {
        return TagResponseDTO.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .build();
    }
}