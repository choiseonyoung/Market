package com.example.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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

}
