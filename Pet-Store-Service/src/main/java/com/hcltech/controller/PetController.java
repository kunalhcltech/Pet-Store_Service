package com.hcltech.controller;

import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.service.PetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-api")
public class PetController {

    @Autowired
    private PetServiceImpl petServiceImpl;
    @PostMapping("/create")
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetRequestDTO petRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.createPet(petRequestDTO));
    }
    @PutMapping("/update-price")
    public ResponseEntity<PetResponseDTO> updatePetPriceById(@RequestParam("id") Long id,@RequestParam("price") Double price)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.updatePetPriceById(id,price));
    }
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<String> deletePetById(@PathVariable("petId") Long petId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.deletePet(petId));
    }
    @GetMapping("/getByCategory/{category}")
    public  ResponseEntity<List<PetResponseDTO>> getPetByCategory(@PathVariable("category") Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getPetByCategory(categoryId));
    }

    @GetMapping("/get/{id}")
    public  ResponseEntity<PetResponseDTO> getById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getPetById(id));
    }
    @GetMapping("/get")
    public  ResponseEntity<List<PetResponseDTO>> getAllPets(){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getAllPets());
    }
}