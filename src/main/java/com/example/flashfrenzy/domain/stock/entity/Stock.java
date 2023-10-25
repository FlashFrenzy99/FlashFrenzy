package com.example.flashfrenzy.domain.stock.entity;

import com.example.flashfrenzy.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Long stock;

    public Stock(Long stock, Product product) {
        this.stock = stock;
        this.product = product;
    }

    public void decrease(Long stock) {
        if (this.stock < stock) {
            throw new IllegalArgumentException("재고가 남아있지 않습니다. 남은재고: " + this.stock);
        }
        this.stock -= stock;
    }

    public void increase(Long stock) {
        this.stock = stock;
    }

}
