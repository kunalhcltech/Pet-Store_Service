package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequestDTO {

    private String petName;

    private Integer age;

    private String breed;

    private String gender;

    private Double price;

  private Long categoryId;

  private Long tagId;
}
