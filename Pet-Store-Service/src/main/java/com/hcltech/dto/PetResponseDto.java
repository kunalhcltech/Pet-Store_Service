package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDTO {
    private Long petId;

    private String petName;

    private Integer age;

    private String breed;

    private String gender;

    private Double price;

    private boolean available;

    private CategoryResponseDTO category;

    private Set<TagResponseDTO> tags;
}
