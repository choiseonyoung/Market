package com.example.market.domain.negotiation.dto;

import com.example.market.domain.negotiation.entity.Negotiation;
import com.example.market.domain.negotiation.entity.NegotiationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NegotiationResponseDTO {

    private Long id;

    private Integer suggestedPrice;

    private NegotiationStatus status;

    public static NegotiationResponseDTO fromEntity(Negotiation entity) {
        NegotiationResponseDTO dto = NegotiationResponseDTO.builder()
                .id(entity.getId())
                .suggestedPrice(entity.getSuggestedPrice())
                .status(entity.getStatus()).build();
        return dto;
    }

}
