package com.hcltech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class PetServiceImpl implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @Autowired
    private TagServiceImpl tagServiceImpl;

    @Autowired
    private ModelMapper mapper;

    @Override
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        logger.info("Entering createPet method");
        if (petRequestDTO == null) {
            throw new InvalidOperationExcepetion("Pet request cannot be null.");
        }
        if (petRequestDTO.getCategoryId() == null) {
            throw new InvalidOperationExcepetion("Category ID cannot be null.");
        }
        CategoryResponseDTO categoryResponseDTO = categoryServiceImpl.getCategoryById(petRequestDTO.getCategoryId());
        Category categoryResult = Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();

        if (petRequestDTO.getTagId() != null && !petRequestDTO.getTagId().isEmpty()) {
            Set<TagResponseDTO> tagResponseDTO = petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
            Set<Tag> tagsList = tagResponseDTO.stream()
                    .map(tagResponseDTO1 -> Tag.builder()
                            .tagId(tagResponseDTO1.getTagId())
                            .tagName(tagResponseDTO1.getTagName())
                            .build())
                    .collect(Collectors.toSet());

            Pet pet = Pet.builder()
                    .petName(petRequestDTO.getPetName())
                    .price(petRequestDTO.getPrice())
                    .age(petRequestDTO.getAge())
                    .gender(petRequestDTO.getGender())
                    .breed(petRequestDTO.getBreed())
                    .category(categoryResult)
                    .tags(tagsList)
                    .available(true)
                    .build();
            System.out.println("*******************"+tagsList);
            logger.info("Saving pet with tags");
            return mapToResponseDTO(petRepository.save(pet));
        } else {
            Pet pet = Pet.builder()
                    .petName(petRequestDTO.getPetName())
                    .price(petRequestDTO.getPrice())
                    .age(petRequestDTO.getAge())
                    .gender(petRequestDTO.getGender())
                    .breed(petRequestDTO.getBreed())
                    .category(categoryResult)
                    .available(true)
                    .build();
            logger.info("Saving pet without tags");
            return mapToResponseDTO(petRepository.save(pet));
        }
    }

    @Override
    public List<PetResponseDTO> getAllPets() {
        logger.info("Fetching all available pets");
        List<Pet> pets = petRepository.findByAvailableTrue();
        return pets.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public PetResponseDTO getPetById(Long id) {
        logger.info("Fetching pet by ID: " + id);
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided.");
        }
        return mapToResponseDTO(petRepository.findByPetIdAndAvailableTrue(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id)));
    }

    @Override
    public PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO) {
        logger.info("Updating pet with ID: " + id);
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for update.");
        }
        if (petRequestDTO == null) {
            throw new InvalidOperationExcepetion("Pet request cannot be null for update.");
        }
        if (petRequestDTO.getCategoryId() == null) {
            throw new InvalidOperationExcepetion("Category ID cannot be null for update.");
        }

        CategoryResponseDTO categoryResponseDTO = categoryServiceImpl.getCategoryById(petRequestDTO.getCategoryId());
        Category categoryResult = Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();

        Set<Tag> tagsList = null;
        if (petRequestDTO.getTagId() != null && !petRequestDTO.getTagId().isEmpty()) {
            Set<TagResponseDTO> tagResponseDTO = petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
            tagsList = tagResponseDTO.stream()
                    .map(tagResponseDTO1 -> Tag.builder()
                            .tagId(tagResponseDTO1.getTagId())
                            .tagName(tagResponseDTO1.getTagName())
                            .build())
                    .collect(Collectors.toSet());
        }

        Pet pet = petRepository.findByPetIdAndAvailableTrue(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
        pet.setPetName(petRequestDTO.getPetName());
        pet.setAge(petRequestDTO.getAge());
        pet.setBreed(petRequestDTO.getBreed());
        pet.setPrice(petRequestDTO.getPrice());
        pet.setGender(petRequestDTO.getGender());
        pet.setCategory(categoryResult);
        if (tagsList != null) {
            pet.setTags(tagsList);
        }
        logger.info("Pet updated successfully with ID: " + id);
        return mapToResponseDTO(petRepository.save(pet));
    }

    @Override
    public String deletePet(Long id) {
        logger.info("Deleting pet with ID: " + id);
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for deletion.");
        }
        Pet pet = petRepository.findByPetIdAndAvailableTrue(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
        petRepository.delete(pet);
        return "Pet with ID " + id + " has been removed.";
    }

    @Override
    public List<PetResponseDTO> getPetByCategory(Long id) {
        logger.info("Fetching pets by category ID: " + id);
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid category ID provided.");
        }
        List<Pet> pets = petRepository.findByAvailableTrue().stream()
                .filter(pet1 -> pet1.getCategory() != null && Objects.equals(pet1.getCategory().getCategoryId(), id))
                .toList();
        if (pets.isEmpty()) {
            logger.warn("No pets found for category ID: " + id);
            return new ArrayList<>();
        }
        return pets.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public PetResponseDTO updatePetPriceById(Long id, Double price) {
        logger.info("Updating price for pet ID: " + id);
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for price update.");
        }
        if (price == null || price < 0) {
            throw new InvalidOperationExcepetion("Price cannot be null or negative.");
        }
        Pet pet = petRepository.findByPetIdAndAvailableTrue(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
        pet.setPrice(price);
        return mapToResponseDTO(petRepository.save(pet));
    }

    public PetResponseDTO mapToResponseDTO(Pet pet) {
        return PetResponseDTO.builder()
                .petId(pet.getPetId())
                .age(pet.getAge())
                .available(pet.getAvailable())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .price(pet.getPrice())
                .petName(pet.getPetName())
                .category(categoryServiceImpl.mapToResponseDTO(pet.getCategory()))
                .tags(pet.getTags() != null ? pet.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()) : null)
                .build();
    }
}
