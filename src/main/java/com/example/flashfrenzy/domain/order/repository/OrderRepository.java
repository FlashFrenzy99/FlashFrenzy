package com.example.flashfrenzy.domain.order.repository;

import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);
}
