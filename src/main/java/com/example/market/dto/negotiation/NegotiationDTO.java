package com.example.market.dto.negotiation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NegotiationDTO {

    @NotBlank
    private String writer;

    @NotBlank
    private String password;

    private Integer suggestedPrice;

    private String status;

}
