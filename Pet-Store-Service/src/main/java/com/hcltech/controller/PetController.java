package com.hcltech.controller;

import com.hcltech.dto.PetRequestDTO;
import com.hcltech.dto.PetResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet-api")
public class PetController {

    @PostMapping("/create")
    public ResponseEntity<PetResponseDTO> createPet(@RequestBody PetRequestDTO petRequestDTO)
    {
        return null;
    }
    @PutMapping("/update-price/{id}/{price}")
    public ResponseEntity<PetResponseDTO> updatePetPriceById(@PathVariable("id") Integer id,@PathVariable("price") Double price)
    {
        return null;
    }
    @DeleteMapping("/delete/{petId}")
        public ResponseEntity<String> deletePetById(@PathVariable("petId") Integer petId)
        {
            return null;
        }
    @GetMapping("/getByCategory/{category}")
    public  ResponseEntity<List<PetResponseDTO>> getPetByCategory(@PathVariable("category") String category){
        return null;
    }

    @GetMapping("/get/{id}")
    public  ResponseEntity<PetResponseDTO> getById(@PathVariable("id") Integer id){
        return null;
    }
    @GetMapping("/get")
    public  ResponseEntity<List<PetResponseDTO>> getAllPets(){
        return null;
    }
}
