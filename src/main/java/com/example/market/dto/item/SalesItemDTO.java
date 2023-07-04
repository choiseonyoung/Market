package com.example.market.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SalesItemDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String iamgeUrl;

    @NotNull
    private Integer minPriceWanted;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

}
