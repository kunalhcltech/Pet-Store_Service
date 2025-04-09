package com.hcltech.repository;

import com.hcltech.model.Category;
import com.hcltech.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long> {
}