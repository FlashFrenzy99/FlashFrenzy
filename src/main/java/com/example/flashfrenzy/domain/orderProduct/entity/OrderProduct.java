package com.example.flashfrenzy.domain.orderProduct.entity;

import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.product.entity.Product;
import jakarta.persistence.*;
import java.awt.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @Column(name = "order_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "count", nullable = false)
    private Long count;  // 주문 수량

    private Long price;

    public OrderProduct(BasketProduct basketProduct) {
        this.product = basketProduct.getProduct();
        this.count = basketProduct.getCount();
        this.price = basketProduct.getProduct().getPrice();
    }

    public OrderProduct(BasketProduct basketProduct,int saleRate) {
        this.product = basketProduct.getProduct();
        this.count = basketProduct.getCount();
        this.price = basketProduct.getProduct().getPrice() * (100 - saleRate) / 100;
    }

    public void addOrder(Order order) {
        this.order = order;
    }
}
