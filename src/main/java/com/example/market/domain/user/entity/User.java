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

    // 로그인 id
    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 10)
    private String password;

    // 이름
    @Column(nullable = false, length = 20)
    private String name;

    // 닉네임
    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 11)
    private String phoneNumber;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 10)
    private String address;

    @OneToMany(mappedBy = "user")
    private List<SalesItem> salesItems;

    @OneToMany(mappedBy = "user")
    private List<Negotiation> negotiations;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @Builder
    public User(Long id, String username, String password, String name, String nickname, String email, String phoneNumber, Gender gender, String address, List<SalesItem> salesItems, List<Negotiation> negotiations, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.salesItems = salesItems;
        this.negotiations = negotiations;
        this.comments = comments;
    }
}
