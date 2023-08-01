package com.example.market.entity;

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

    private Long itemId;

    private Integer suggestedPrice;

    // 수락, 거절, 확정
    private String status;

    private String writer;

    private String password;

    @ManyToOne
    private SalesItem salesItem;

    // 제안자
    @ManyToOne
    private User user;


}
