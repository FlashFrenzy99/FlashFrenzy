package com.example.flashfrenzy.domain.order.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("장바구니에 담은 물품이 없으면 주문을 생성할 수 없다.")
    void OrderWithoutProduct() {
        //given
        User user = new User("test", "123", UserRoleEnum.USER, "hello@naver.com", new Basket());

        User savedUser = userRepository.save(user);

        //when - then
        Assertions.assertThatThrownBy(() ->
                        orderService.orderBasketProducts(savedUser.getBasket().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니에 물품이 1개 이상 존재해야 주문이 가능합니다.");
    }

}