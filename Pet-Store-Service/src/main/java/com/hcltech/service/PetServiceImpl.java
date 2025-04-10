package com.hcltech.service;

import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.TagNoFoundException;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.PetRepository;
import com.hcltech.repository.TagRepository;
import org.modelmapper.ModelMapper;
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
    private TagRepository tagRepository;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @Autowired
    private TagServiceImpl tagServiceImpl;

    @Autowired
    private ModelMapper mapper;
    @Override
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        CategoryResponseDTO categoryResponseDTO=categoryServiceImpl.
                getCategoryById(petRequestDTO.getCategoryId());
        Category categoryResult=Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();

        List<Long> tagId = petRequestDTO.getTagId();
        Set<Tag> tags = tagId.stream().map(id -> mapper.map(tagRepository.findById(id).orElseThrow(() -> new TagNoFoundException("Invalid Tag id")), Tag.class)).collect(Collectors.toSet());

        //Set<TagResponseDTO> tagResponseDTO =petRequestDTO.getTagId().stream().map().collect(Collectors.toSet());
//        Set<Tag>tags=tagResponseDTO.stream().map(tagServiceImpl::mapToResponseDTO).collect(C)
//                Tag.builder().tagId(categoryResponseDTO.getCategoryId()).tagName(categoryResponseDTO.getCategoryName()).build();

        Pet pet = Pet.builder()
                .petName(petRequestDTO.getPetName())
                .price(petRequestDTO.getPrice())
                .age(petRequestDTO.getAge())
                .gender(petRequestDTO.getGender())
                .breed(petRequestDTO.getBreed())
                .category(categoryResult)
                .tags(tags)
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
        return mapToResponseDTO(petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found")));
    }

    @Override
    public PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO) {
        CategoryResponseDTO categoryResponseDTO=categoryServiceImpl.
                getCategoryById(petRequestDTO.getCategoryId());
        Category categoryResult=Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();
        Set<TagResponseDTO> tagResponseDTO =petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
        Set<Tag>tagsList=tagResponseDTO.stream()
                .map(tagResponseDTO1 -> Tag.builder()
                        .tagId(tagResponseDTO1.getTagId())
                        .tagName(tagResponseDTO1.getTagName())
                        .build())
                .collect(Collectors.toSet());

        Pet pet=petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found"));
        pet.setPetName(petRequestDTO.getPetName());
        pet.setAge(petRequestDTO.getAge());
        pet.setBreed(petRequestDTO.getBreed());
        pet.setPrice(petRequestDTO.getPrice());
        pet.setGender(petRequestDTO.getGender());
        pet.setCategory(categoryResult);
        pet.setTags(tagsList);
        return mapToResponseDTO(petRepository.save(pet));
    }

    @Override
    public void deletePet(Long id) {
        Pet pet=petRepository.findById(id).orElseThrow(()->new RuntimeException("Pet not found"));
        petRepository.delete(pet);
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