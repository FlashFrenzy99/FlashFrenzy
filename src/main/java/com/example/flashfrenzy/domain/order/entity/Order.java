package com.example.flashfrenzy.domain.order.entity;

import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.global.entity.TimeStamp;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends TimeStamp {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProductList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long totalPrice = 0L;

    public void addUser(User user) {
        this.user = user;
        user.getOrderList().add(this);
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        addTotalPrice(orderProduct.getPrice()*orderProduct.getCount());
        this.orderProductList.add(orderProduct);
        orderProduct.addOrder(this);
    }

    private void addTotalPrice(Long price) {
        this.totalPrice += price;
    }
}
