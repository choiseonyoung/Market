package com.example.market.domain.user.dto;

import com.example.market.domain.user.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;

    @NotBlank
    private String name;

    private String nickname;

    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Gender gender;

    private String address;

    @Builder
    public SignupRequestDTO(String username, String password, String passwordCheck, String name, String nickname, String email, String phoneNumber, Gender gender, String address) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
    }
}
