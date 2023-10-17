package com.example.flashfrenzy.domain.product.repository;

import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품 목록 조회")
    @Test
    void getProduct() {
        // given
        productRepository.save(product());
        // when
        List<Product> productList = productRepository.findAll();
        // then
        assertThat(productList.size()).isEqualTo(1);
    }

    private Product product() {
        return new Product(1L, "Test", "TestImage", 10000L, "의류", "바지", 100L);
    }
}
