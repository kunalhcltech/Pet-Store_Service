package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO {
    private Long customerId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private List<OrderResponseDto> purchases;
}
