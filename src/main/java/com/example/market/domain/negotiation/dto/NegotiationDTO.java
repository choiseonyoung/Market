package com.example.market.domain.negotiation.dto;

import com.example.market.domain.negotiation.entity.Negotiation;
import com.example.market.domain.negotiation.entity.NegotiationStatus;
import com.example.market.domain.salesitem.entity.SalesItem;
import com.example.market.domain.user.entity.User;
import lombok.Getter;

@Getter
public class NegotiationDTO {

    private String writer;

    private String password;

    private Integer suggestedPrice;

    private NegotiationStatus status;

    public Negotiation toEntity(SalesItem salesItem, User user) {
        return Negotiation.builder()
                .suggestedPrice(suggestedPrice)
                .status(NegotiationStatus.PROPOSAL)
                .salesItem(salesItem)
                .user(user)
                .build();
    }

}
