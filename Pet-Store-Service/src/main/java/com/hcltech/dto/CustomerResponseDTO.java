package com.hcltech.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseDTO {
    private Long customerId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private List<OrderResponseDTO> purchases;
}
