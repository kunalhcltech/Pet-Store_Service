package com.hcltech.service;

import com.hcltech.dto.TagRequestDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.PetRepository;
import com.hcltech.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PetRepository petRepository;

    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequestDTO) {
        logger.info("Creating tag: ", tagRequestDTO);
        if (tagRequestDTO == null) {
            logger.warn("Tag request is null");
            throw new InvalidOperationExcepetion("Tag request cannot be null.");
        }
        Tag tag = Tag.builder().tagName(tagRequestDTO.getTagName()).build();
        return mapToResponseDTO(tagRepository.save(tag));
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        logger.info("Fetching all tags");
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        logger.info("Fetching tag by ID: {}", id);
        if (id == null || id <= 0) {
            logger.warn("Invalid tag ID provided: ", id);
            throw new InvalidOperationExcepetion("Invalid tag ID provided.");
        }
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + id));
        return mapToResponseDTO(tag);
    }

    @Override
    public String deleteTag(Long id) {
        logger.info("Deleting tag with ID: ", id);
        if (id == null || id <= 0) {
            logger.warn("Invalid tag ID provided for deletion: {}", id);
            throw new InvalidOperationExcepetion("Invalid tag ID provided for deletion.");
        }
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag not found with ID: " + id));

        List<Pet> pets = petRepository.findAll();
        for (Pet pet : pets) {
            pet.getTags().remove(tag);
        }
        petRepository.saveAll(pets);
        tagRepository.delete(tag);
        logger.info("Tag deleted successfully: ", id);
        return "Tag with ID " + id + " has been deleted successfully";
    }

    public TagResponseDTO mapToResponseDTO(Tag tag) {
        return TagResponseDTO.builder().tagId(tag.getTagId()).tagName(tag.getTagName()).build();
    }
}
