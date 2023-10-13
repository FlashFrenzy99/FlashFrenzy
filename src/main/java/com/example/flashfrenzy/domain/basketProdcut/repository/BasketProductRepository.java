package com.example.flashfrenzy.domain.basketProdcut.repository;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
    void deleteAllByBasket(Basket basket);
    Optional<BasketProduct> findByBasketAndProduct(Basket basket, Product product);
}
