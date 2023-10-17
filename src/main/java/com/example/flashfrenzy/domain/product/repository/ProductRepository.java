package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

import com.example.flashfrenzy.global.elastic.CustomProductSearchRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface ProductRepository extends ElasticsearchRepository<Product, Long>, CustomProductSearchRepository {

    List<Product> findAllByTitleContains(String query);
//    Page<Product> findAllByTitleContains(String query, Pageable);
    List<Product> findTop2000By();
    List<Product> findByIdIn(List<Long> idList);
    List<Product> findTop2000ByCategory1(String cate);
    Optional<Product> findById(Long productId);
    void save(List<Product> productList);
    Long count();
}
