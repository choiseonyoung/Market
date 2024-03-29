package com.example.market.domain.salesitem.dto;

import com.example.market.domain.salesitem.entity.SalesItem;
import com.example.market.domain.salesitem.entity.SalesItemStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponseDTO {
    private String title;
    private String description;
    private Integer minPriceWanted;
    private String imageUrl;
    private SalesItemStatus status;

    public static ItemResponseDTO fromEntity(SalesItem entity) {
        ItemResponseDTO dto = ItemResponseDTO.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .minPriceWanted(entity.getMinPriceWanted())
                .imageUrl(entity.getIamgeUrl())
                .status(entity.getStatus()).build();
        return dto;
    }
}
