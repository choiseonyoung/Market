package com.example.market.dto.negotiation;

import com.example.market.entity.Negotiation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NegotiationResponseDTO {

    private Long id;

    private Integer suggestedPrice;

    private String status;

    public static NegotiationResponseDTO fromEntity(Negotiation entity) {
        NegotiationResponseDTO dto = NegotiationResponseDTO.builder()
                .id(entity.getId())
                .suggestedPrice(entity.getSuggestedPrice())
                .status(entity.getStatus()).build();
        return dto;
    }

}
