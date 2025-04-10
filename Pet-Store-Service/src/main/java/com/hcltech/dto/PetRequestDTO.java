package com.hcltech.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PetRequestDTO {

    @NotBlank(message = "Pet name cannot be blank")
    @Size(min = 2, max = 50, message = "Pet name must be between 2 and 50 characters")
    private String petName;

    @NotNull(message = "Age cannot be null")
    @Min(value = 0, message = "Age must be a non-negative value")
    private Integer age;

    @NotBlank(message = "Breed cannot be blank")
    @Size(min = 2, max = 50, message = "Breed must be between 2 and 50 characters")
    private String breed;

    @NotBlank(message = "Gender cannot be blank")
    @Size(min = 1, max = 20, message = "Gender must be between 1 and 20 characters")
    private String gender;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be a positive number")
    private Long categoryId;

    @NotEmpty(message = "Tag IDs cannot be empty")
    private Set<@Positive(message = "Tag ID must be a positive number") Long> tagId;

}