package com.example.flashfrenzy.domain.order.service;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "Order API")
public class OrderService {

    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final BasketProductRepository basketProductRepository;

    @Transactional
    public void orderBasketProducts(Long id) {
        log.debug("장바구니 상품 주문");
        Basket basket = basketRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 장바구니가 존재하지 않습니다.")
        );

        User user = basket.getUser();
        List<BasketProduct> basketProductList = basket.getList();
        List<OrderProduct> orderProductList = basketProductList.stream().map(OrderProduct::new)
                .toList();

        Order order = new Order();
        order.addUser(user);
        for (OrderProduct orderProduct : orderProductList) {
            order.addOrderProduct(orderProduct);
            Product product = orderProduct.getProduct();
            product.discountStock(orderProduct.getCount());
        }

        orderRepository.save(order);

        //주문 후 장바구니 내역 삭제
        basketProductRepository.deleteAllByBasket(basket);

    }

}
