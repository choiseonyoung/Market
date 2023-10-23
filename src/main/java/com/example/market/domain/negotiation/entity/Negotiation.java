package com.example.market.domain.negotiation.entity;

import com.example.market.domain.user.entity.User;
import com.example.market.domain.salesitem.entity.SalesItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Negotiation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer suggestedPrice;

    // 제안, 수락, 거절, 확정
    private String status;

    private String writer;

    private String password;

    @ManyToOne
    private SalesItem salesItem;

    // 제안자
    @ManyToOne
    private User user;


}
