package com.example.flashfrenzy.domain.orderProduct.repository;

import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("select distinct op from OrderProduct op join fetch op.product where op.product.stock = 0")
    List<OrderProduct> findAllWithProduct();

}
