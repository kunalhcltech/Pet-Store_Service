//package com.hcltech.service;
//
//import com.hcltech.dto.CategoryResponseDTO;
//import com.hcltech.dto.PetRequestDTO;
//import com.hcltech.dto.PetResponseDTO;
//import com.hcltech.dto.TagResponseDTO;
//import com.hcltech.exceptions.PetNotFoundException;
//import com.hcltech.exceptions.TagNotFoundException;
//import com.hcltech.model.Category;
//import com.hcltech.model.Pet;
//import com.hcltech.model.Tag;
//import com.hcltech.repository.CategoryRepository;
//import com.hcltech.repository.PetRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class PetServiceImpl implements PetService{
//    @Autowired
//    private PetRepository petRepository;
//    @Autowired
//    private CategoryServiceImpl categoryServiceImpl;
//    @Autowired
//    private TagServiceImpl tagServiceImpl;
//
//    @Override
//    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
//        CategoryResponseDTO categoryResponseDTO=categoryServiceImpl.
//                getCategoryById(petRequestDTO.getCategoryId());
//        Category categoryResult=Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();
//
//        Set<TagResponseDTO> tagResponseDTO =petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
//        Set<Tag>tagsList=tagResponseDTO.stream()
//                .map(tagResponseDTO1 -> Tag.builder()
//                        .tagId(tagResponseDTO1.getTagId())
//                        .tagName(tagResponseDTO1.getTagName())
//                        .build())
//                .collect(Collectors.toSet());
//
//        Pet pet = Pet.builder()
//                .petName(petRequestDTO.getPetName())
//                .price(petRequestDTO.getPrice())
//                .age(petRequestDTO.getAge())
//                .gender(petRequestDTO.getGender())
//                .breed(petRequestDTO.getBreed())
//                .category(categoryResult)
//                .tags(tagsList)
//                .build();
//        return null;
//    }
//
//    @Override
//    public List<PetResponseDTO> getAllPets() {
//        List<Pet> pets = petRepository.findAll();
//        return pets.stream()
//                .map(pet -> PetResponseDTO.builder()
//                        .petName(pet.getPetName())
//                        .price(pet.getPrice())
//                        .age(pet.getAge())
//                        .gender(pet.getGender())
//                        .available(pet.getAvailable())
//                        .breed(pet.getBreed())
//                        .category(categoryServiceImpl.mapToResponseDTO(pet.getCategory()))
//                        .tags(pet.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()))
//                        .build())
//                .toList();
//    }
//
//    @Override
//    public PetResponseDTO getPetById(Long id) {
//        return mapToResponseDTO(petRepository.findById(id).orElseThrow(()->new PetNotFoundException("Pet not found")));
//    }
//
//    @Override
//    public PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO) {
//        CategoryResponseDTO categoryResponseDTO=categoryServiceImpl.
//                getCategoryById(petRequestDTO.getCategoryId());
//        Category categoryResult=Category.builder().categoryId(categoryResponseDTO.getCategoryId()).categoryName(categoryResponseDTO.getCategoryName()).build();
//        Set<TagResponseDTO> tagResponseDTO =petRequestDTO.getTagId().stream().map(tagServiceImpl::getTagById).collect(Collectors.toSet());
//        Set<Tag>tagsList=tagResponseDTO.stream()
//                .map(tagResponseDTO1 -> Tag.builder()
//                        .tagId(tagResponseDTO1.getTagId())
//                        .tagName(tagResponseDTO1.getTagName())
//                        .build())
//                .collect(Collectors.toSet());
//
//        Pet pet=petRepository.findById(id).orElseThrow(()->new PetNotFoundException("Pet not found"));
//        pet.setPetName(petRequestDTO.getPetName());
//        pet.setAge(petRequestDTO.getAge());
//        pet.setBreed(petRequestDTO.getBreed());
//        pet.setPrice(petRequestDTO.getPrice());
//        pet.setGender(petRequestDTO.getGender());
//        pet.setCategory(categoryResult);
//        pet.setTags(tagsList);
//        return mapToResponseDTO(petRepository.save(pet));
//    }
//
//    @Override
//    public String deletePet(Long id) {
//        Pet pet=petRepository.findById(id).orElseThrow(()->new PetNotFoundException("Pet not found"));
//        petRepository.delete(pet);
//        return  "Pet has been removed from here";
//    }
//
//    @Override
//    public List<PetResponseDTO> getPetByCategory(Long id) {
//        List<Pet> pets=petRepository.findAll().stream().filter(pet1 -> Objects.equals(pet1.getCategory().getCategoryId(), id)).toList();
//        List<PetResponseDTO> petsList=new ArrayList<>();
//        if(!pets.isEmpty()){
//            return pets.stream().map(this::mapToResponseDTO).toList();
//        }
//        else {
//            return petsList;
//        }
//    }
//
//    @Override
//    public PetResponseDTO updatePetPriceById(Long id,Double price) {
//        Pet pet=petRepository.findById(id).orElseThrow(()->new PetNotFoundException("Pet not found"));
//        if(pet !=null){
//            pet.setPrice(price);
//        }
//        return mapToResponseDTO(petRepository.save(pet));
//    }
//
//    public PetResponseDTO mapToResponseDTO(Pet pet){
//        return PetResponseDTO.builder()
//                .petId(pet.getPetId())
//                .age(pet.getAge())
//                .available(pet.getAvailable())
//                .breed(pet.getBreed())
//                .gender(pet.getGender())
//                .price(pet.getPrice())
//                .petName(pet.getPetName())
//                .category(categoryServiceImpl.mapToResponseDTO(pet.getCategory()))
//                .tags(pet.getTags().stream().map(tagServiceImpl::mapToResponseDTO).collect(Collectors.toSet()))
//                .build();
//    }
//}
package com.hcltech.service;

import com.hcltech.dto.CategoryResponseDTO;
import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.dto.TagResponseDTO;
import com.hcltech.exceptions.InvalidOperationExcepetion;
import com.hcltech.exceptions.PetNotFoundException;
import com.hcltech.exceptions.TagNotFoundException;
import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import com.hcltech.model.Tag;
import com.hcltech.repository.CategoryRepository;
import com.hcltech.repository.PetRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CategoryServiceImpl categoryServiceImpl;
    @Autowired
    private TagServiceImpl tagServiceImpl;

    @Override
    public PetResponseDTO createPet( PetRequestDTO petRequestDTO) {
        if (petRequestDTO == null) {
            throw new InvalidOperationExcepetion("Pet request cannot be null.");
        }
        if (petRequestDTO.getCategoryId() == null) {
            throw new InvalidOperationExcepetion("Category ID cannot be null.");
        }
        CategoryResponseDTO categoryResponseDTO = categoryServiceImpl.
                getCategoryById(petRequestDTO.getCategoryId());
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
                    .available(true) // Assuming new pets are available by default
                    .build();
            return mapToResponseDTO(petRepository.save(pet));
        } else {
            Pet pet = Pet.builder()
                    .petName(petRequestDTO.getPetName())
                    .price(petRequestDTO.getPrice())
                    .age(petRequestDTO.getAge())
                    .gender(petRequestDTO.getGender())
                    .breed(petRequestDTO.getBreed())
                    .category(categoryResult)
                    .available(true) // Assuming new pets are available by default
                    .build();
            return mapToResponseDTO(petRepository.save(pet));
        }
    }

    @Override
    public List<PetResponseDTO> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public PetResponseDTO getPetById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided.");
        }
        return mapToResponseDTO(petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id)));
    }

    @Override
    public PetResponseDTO updatePet(Long id,  PetRequestDTO petRequestDTO) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for update.");
        }
        if (petRequestDTO == null) {
            throw new InvalidOperationExcepetion("Pet request cannot be null for update.");
        }
        if (petRequestDTO.getCategoryId() == null) {
            throw new InvalidOperationExcepetion("Category ID cannot be null for update.");
        }

        CategoryResponseDTO categoryResponseDTO = categoryServiceImpl.
                getCategoryById(petRequestDTO.getCategoryId());
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

        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
        pet.setPetName(petRequestDTO.getPetName());
        pet.setAge(petRequestDTO.getAge());
        pet.setBreed(petRequestDTO.getBreed());
        pet.setPrice(petRequestDTO.getPrice());
        pet.setGender(petRequestDTO.getGender());
        pet.setCategory(categoryResult);
        if (tagsList != null) {
            pet.setTags(tagsList);
        }
        return mapToResponseDTO(petRepository.save(pet));
    }

    @Override
    public String deletePet(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for deletion.");
        }
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
        petRepository.delete(pet);
        return "Pet with ID " + id + " has been removed.";
    }

    @Override
    public List<PetResponseDTO> getPetByCategory(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid category ID provided.");
        }
        List<Pet> pets = petRepository.findAll().stream()
                .filter(pet1 -> pet1.getCategory() != null && Objects.equals(pet1.getCategory().getCategoryId(), id))
                .toList();
        if (pets.isEmpty()) {
            return new ArrayList<>(); // Return empty list if no pets found for the category
        }
        return pets.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public PetResponseDTO updatePetPriceById(Long id, Double price) {
        if (id == null || id <= 0) {
            throw new InvalidOperationExcepetion("Invalid pet ID provided for price update.");
        }
        if (price == null || price < 0) {
            throw new InvalidOperationExcepetion("Price cannot be null or negative.");
        }
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id));
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