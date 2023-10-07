package com.example.flashfrenzy.domain.basket.entity;

import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Basket {

    @Id
    @Column(name = "basket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "basket")
    private List<BasketProduct> list = null;
}
