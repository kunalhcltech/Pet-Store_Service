package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;

import java.util.List;

public interface TagService {
    TagResponseDTO createTag(TagRequestDTO dto);
    List<TagResponseDTO> getAllTags();
    TagResponseDTO getTagById(Long id);
    String deleteTag(Long id);
}