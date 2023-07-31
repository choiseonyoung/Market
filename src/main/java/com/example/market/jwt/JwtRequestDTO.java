package com.example.market.jwt;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;

@Data
public class JwtRequestDTO {
    private String username;
    private String password;
}
