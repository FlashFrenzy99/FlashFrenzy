package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.dto.ProductSearchResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.global.elastic.CustomProductSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;
import java.util.stream.Stream;

@EnableElasticsearchRepositories
public interface ProductSearchRepository extends ElasticsearchRepository<Product,Long> , CustomProductSearchRepository {
    Page<Product> searchByTitle(String title, Pageable pageable);
}
