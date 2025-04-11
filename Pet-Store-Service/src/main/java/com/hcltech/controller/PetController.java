package com.hcltech.controller;

import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import com.hcltech.service.PetServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.OnError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Descriptor;
import java.util.List;

@RestController
@RequestMapping("/pet-api")
@SecurityRequirement(name = "bearerAuth")
public class PetController {

    @Autowired
    private PetServiceImpl petServiceImpl;
    @PostMapping("/create")
    @Operation(summary = "Create Pet",description = "This method creates the pet")
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetRequestDTO petRequestDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.createPet(petRequestDTO));
    }
    @PutMapping("/update-price")
    @Operation(summary = "Update Price",description = "This method updates the price of pet")
    public ResponseEntity<PetResponseDTO> updatePetPriceById(@RequestParam("id") Long id,@RequestParam("price") Double price)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.updatePetPriceById(id,price));
    }
    @DeleteMapping("/delete/{petId}")
    @Operation(summary = "Delete Pet",description ="this method will delete the pet with reference of ID")
    public ResponseEntity<String> deletePetById(@PathVariable("petId") Long petId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.deletePet(petId));
    }
    @GetMapping("/getByCategory/{category}")
    @Operation(summary = "Get Pet by Category",description = "This method will get all pets of category")
    public  ResponseEntity<List<PetResponseDTO>> getPetByCategory(@PathVariable("category") Long categoryId){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getPetByCategory(categoryId));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get Pet by ID",description = "This method will get pet with reference if ID")
    public  ResponseEntity<PetResponseDTO> getById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getPetById(id));
    }
    @GetMapping("/get")
    @Operation(summary = "Get All Pets",description = "This method will get all pets")
    public  ResponseEntity<List<PetResponseDTO>> getAllPets(){
        return ResponseEntity.status(HttpStatus.OK).body(petServiceImpl.getAllPets());
    }
}