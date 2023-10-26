package com.example.flashfrenzy.domain.order.repository;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.service.OrderService;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저가 주문한 모든 주문들을 조회한다.")
    void findAllByUser() {
        //given
        User user = new User("test","123", UserRoleEnum.USER,"hello@naver.com",new Basket());
        User savedUser = userRepository.save(user);

        Order order1 = new Order();
        Order order2 = new Order();
        order1.addUser(user);
        order2.addUser(user);

        List<Order> orders = orderRepository.saveAll(List.of(order1, order2));

        //when
        Page<Order> findOrders = orderRepository.findAllByUser(user);

        //then
        Assertions.assertThat(orders).hasSize(2)
                .extracting("user")
                .containsExactlyInAnyOrder(
                        savedUser,
                        savedUser
                );
    }

}