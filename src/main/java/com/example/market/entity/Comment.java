package com.example.market.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String writer;

    private String password;

    private String content;

    private String reply;

    @ManyToOne
    private SalesItem salesItem;

    // 댓글 작성자
    @ManyToOne
    private User user;


}
