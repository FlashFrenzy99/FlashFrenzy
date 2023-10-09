package com.example.flashfrenzy.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "category1", nullable = false)
    private String category1; // 대분류  ex) 옷

    @Column(name = "category2", nullable = false)
    private String category2; // 중분류  ex) 하의, 상의

    @Column(name = "stock", nullable = false)
    private Long stock;

    public void discountStock(Long stock) {
        if (this.stock < stock) {
            throw new IllegalArgumentException("재고가 남아있지 않습니다. 남은재고: "+ this.stock );
        }
        this.stock -= stock;
    }
}
