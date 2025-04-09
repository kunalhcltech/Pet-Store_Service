package com.hcltech.service;

import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.CategoryRepository;
import com.hcltech.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService{
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @Autowired
    private TagServiceImpl tagServiceImpl;

    @Override
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        CategoryResponseDTO categoryResponseDTO=categoryServiceImpl.
                getCategoryById(petRequestDTO.getCategoryId());
        Category categoryResult=Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();

        Set<TagResponseDTO> tagResponseDTO =petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
//        Set<Tag>tags=tagResponseDTO.stream().map(tagServiceImpl::mapToResponseDTO).collect(C)
//                Tag.builder().tagId(categoryResponseDTO.getCategoryId()).tagName(categoryResponseDTO.getCategoryName()).build();

        Pet pet = Pet.builder()
                .petName(petRequestDTO.getPetName())
                .price(petRequestDTO.getPrice())
                .age(petRequestDTO.getAge())
                .gender(petRequestDTO.getGender())
                .breed(petRequestDTO.getBreed())
                .category(categoryResult)
                //.tags(petRequestDTO.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()))
                .build();
        return null;
    }

    @Override
    public List<PetResponseDTO> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream()
                .map(pet -> PetResponseDTO.builder()
                        .petName(pet.getPetName())
                        .price(pet.getPrice())
                        .age(pet.getAge())
                        .gender(pet.getGender())
                        .available(pet.getAvailable())
                        .breed(pet.getBreed())
                        .category(categoryServiceImpl.mapToResponseDTO(pet.getCategory()))
                        .tags(pet.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()))
                        .build())
                .toList();
    }

    @Override
    public PetResponseDTO getPetById(Long id) {
        return null;
    }

    @Override
    public PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO) {
        return null;
    }

    @Override
    public void deletePet(Long id) {

    }

    public PetResponseDTO mapToResponseDTO(Pet pet){
        return PetResponseDTO.builder()
                .petId(pet.getPetId())
                .age(pet.getAge())
                .available(pet.getAvailable())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .price(pet.getPrice())
                .petName(pet.getPetName())
                .category(categoryServiceImpl.mapToResponseDTO(pet.getCategory()))
                .tags(pet.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()))
                .build();
    }
}
