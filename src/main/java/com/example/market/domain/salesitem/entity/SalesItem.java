package com.example.market.domain.salesitem.entity;

import com.example.market.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String iamgeUrl;

    private Integer minPriceWanted;

    // 판매중, 판매완료
    private String status;

    private String writer;

    private String password;

    @ManyToOne
    private User user;
}
