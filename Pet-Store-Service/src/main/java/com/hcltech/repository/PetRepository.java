package com.hcltech.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcltech.model.Category;
import com.hcltech.model.Customer;
import com.hcltech.model.Pet;

public interface PetRepository extends JpaRepository<Pet,Long> {
    List<Pet> findByCategory(Category category);
    List<Pet> findByCategoryAndAvailableTrue(Category category);
    List<Pet> findByAvailableTrue();
    Optional<Pet> findByPetIdAndAvailableTrue(Long petId);
}