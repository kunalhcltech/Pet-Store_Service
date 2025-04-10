package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CustomerRequestDTO {

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String customerPassword;
}
