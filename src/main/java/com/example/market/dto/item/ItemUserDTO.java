package com.example.market.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ItemUserDTO {

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

}