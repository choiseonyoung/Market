package com.example.market.dto.negotiation;

import com.example.market.entity.Negotiation;
import com.example.market.entity.SalesItem;
import com.example.market.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NegotiationDTO {

    private String writer;

    private String password;

    private Integer suggestedPrice;

    private String status;

    public Negotiation toEntity(SalesItem salesItem, User user) {
        return Negotiation.builder()
                .suggestedPrice(suggestedPrice)
                .status("제안")
                .salesItem(salesItem)
                .user(user)
                .build();
    }

}
