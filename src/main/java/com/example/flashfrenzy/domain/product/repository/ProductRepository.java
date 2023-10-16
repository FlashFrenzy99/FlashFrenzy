package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByTitleContains(String query);
//    Page<Product> findAllByTitleContains(String query, Pageable);

    List<Product> findTop2000By();

}
