package com.example.flashfrenzy.global.elastic;

import com.example.flashfrenzy.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductSearchRepository {

    Page<Product> searchByTitle(String title, Pageable pageable);
}
