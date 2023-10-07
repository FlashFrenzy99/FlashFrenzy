package com.example.flashfrenzy.domain.order.repository;

import com.example.flashfrenzy.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
