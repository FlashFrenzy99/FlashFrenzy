package com.example.flashfrenzy.domain.orderProduct.repository;

import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
