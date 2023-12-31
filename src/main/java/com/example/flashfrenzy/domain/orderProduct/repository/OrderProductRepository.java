package com.example.flashfrenzy.domain.orderProduct.repository;

import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("select distinct op from OrderProduct op left join Stock s on op.product = s.product where s.stock = 0")
    List<OrderProduct> findAllWithZeroStock();

    @Query("SELECT op.product FROM OrderProduct op GROUP BY op.product.id ORDER BY COUNT(op.product.id) DESC limit 5")
    List<Product> findTop5Order();
}
