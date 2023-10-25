package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByTitleContains(String query);
//    Page<Product> findAllByTitleContains(String query, Pageable);

//    List<Product> findTop2000By();

    List<Product> findByIdIn(List<Long> idList);


    @Query("select p from Product p where p.category1 =:category1")
    Page<Product> findAllByCategory1(@Param("category1") String category1, Pageable pageable);

    @Query("select p from Product p")
    Stream<Product> streamAllPaged(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.title LIKE %:query%")
    Stream<Product> findAllByCustomQueryAndStream(@Param("query") String query, Pageable pageable);

}
