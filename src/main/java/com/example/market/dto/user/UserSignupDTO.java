package com.example.market.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignupDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;
    private String phoneNumber;
    private String email;
    private String address;
}
