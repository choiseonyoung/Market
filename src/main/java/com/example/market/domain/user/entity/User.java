package com.example.market.domain.user.entity;

import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.negotiation.entity.Negotiation;
import com.example.market.domain.salesitem.entity.SalesItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    private String phoneNumber;
    private String email;
    private String address;

    @OneToMany(mappedBy = "user")
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "user")
    private List<Negotiation> negotiations;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @Builder
    public User(Long id, String username, String password, String phoneNumber, String email, String address, List<SalesItem> salesItems, List<Negotiation> negotiations, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.salesItems = salesItems;
        this.negotiations = negotiations;
        this.comments = comments;
    }
}
