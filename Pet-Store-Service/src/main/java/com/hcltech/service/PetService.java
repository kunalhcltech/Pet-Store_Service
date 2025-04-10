package com.hcltech.service;

import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;

import java.util.List;

public interface PetService {

    PetResponseDTO createPet(PetRequestDTO petRequestDTO);
    List<PetResponseDTO> getAllPets();
    PetResponseDTO getPetById(Long id);
    PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO);
    String deletePet(Long id);

    PetResponseDTO updatePetPriceById(Long id,Double price);

    List<PetResponseDTO> getPetByCategory(Long id);
}
