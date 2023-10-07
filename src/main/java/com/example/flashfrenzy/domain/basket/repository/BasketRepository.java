package com.example.flashfrenzy.domain.basket.repository;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
