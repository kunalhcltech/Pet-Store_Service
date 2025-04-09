package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long purchaseId;

    private CustomerResponseDTO customerDto;

    private PetResponseDTO pet;

    private LocalDateTime purchaseDate;

    private boolean returned;
}
