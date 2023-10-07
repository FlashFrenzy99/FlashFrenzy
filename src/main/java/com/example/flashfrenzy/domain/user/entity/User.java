package com.example.flashfrenzy.domain.user.entity;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.global.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends TimeStamp {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Basket basket;

    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();
}