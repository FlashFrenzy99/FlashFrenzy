package com.example.flashfrenzy.domain.basketProdcut.entity;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.global.entity.TimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor // test
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE basket_product SET deleted_at = CURRENT_TIMESTAMP where basket_product_id = ?")
public class BasketProduct extends TimeStamp{

    @Id
    @Column(name = "basket_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count",nullable = false)
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public BasketProduct(Long count, Basket basket, Product product) {
        this.count = count;
        this.basket = basket;
        this.product = product;
    }

    public void countUpdate(Long count) {
        this.count = count;
    }
}
