package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIdIn(Set<Long> idList);

    @Query("select p from Product p where p.category1 =:category1")
    Page<Product> findAllByCategory1(@Param("category1") String category1, Pageable pageable);

    @Query("select p from Product p")
    Stream<Product> streamAllPaged(Pageable pageable);

    Product findProductById(Long id);
}