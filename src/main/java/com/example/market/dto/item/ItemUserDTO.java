package com.example.market.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ItemUserDTO {

    private String writer;

    private String password;

}
