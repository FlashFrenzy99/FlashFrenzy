package com.example.flashfrenzy.domain.stock.repository;

import com.example.flashfrenzy.domain.stock.entity.Stock;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("select distinct s from Stock s left join OrderProduct op on op.product = s.product where s.stock = 0")
    Set<Stock> findAllWithZeroStock();
}
