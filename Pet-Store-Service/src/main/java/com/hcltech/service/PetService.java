package com.hcltech.service;

import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;

import java.util.List;

public interface PetService {

    PetResponseDTO createPet(PetRequestDTO petRequestDTO);
    List<PetResponseDTO> getAllPets();
    PetResponseDTO getPetById(Long id);
    PetResponseDTO updatePet(Long id, PetRequestDTO petRequestDTO);
    void deletePet(Long id);
}
