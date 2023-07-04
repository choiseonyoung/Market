package com.example.market.dto.negotiation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NegotiationDTO {

    @NotNull
    private Integer suggestedPrice;

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

}
