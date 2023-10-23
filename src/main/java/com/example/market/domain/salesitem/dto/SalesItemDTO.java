package com.example.market.domain.salesitem.dto;

import com.example.market.domain.salesitem.entity.SalesItem;
import com.example.market.domain.user.entity.User;
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

    private String writer;

    private String password;

    public SalesItem toEntity(User user) {
        return SalesItem.builder()
                .title(title)
                .description(description)
                .iamgeUrl(iamgeUrl)
                .minPriceWanted(minPriceWanted)
                .status("판매중")
                .user(user)
                .build();
    }

}
