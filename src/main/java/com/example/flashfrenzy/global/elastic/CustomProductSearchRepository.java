package com.example.flashfrenzy.global.elastic;

import com.example.flashfrenzy.domain.product.entity.Product;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomProductSearchRepository {

    List<Product> searchByTitle(String title, Pageable pageable);
}
