package com.example.flashfrenzy.global.elastic;

import com.example.flashfrenzy.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Stream;

public interface CustomProductSearchRepository {
    Page<Product> searchByTitle(String title, Pageable pageable);
}
