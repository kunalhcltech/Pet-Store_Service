package com.hcltech.dto;

import lombok.Data;

@Data
public class JWTAuthRequest {
    private String email;
    private String password;
}
