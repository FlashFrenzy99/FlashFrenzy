package com.example.flashfrenzy.product.service;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {

    }

    private final List<Product> productList() { //test 용 상품 리스트 생성
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = new Product((long) i + 1, "Test" + Integer.toString(i + 1), "TestImage", 10000L, "의류", "바지", 100L);
            productList.add(product);
        }
        return productList;
    }

    @DisplayName("DB에 현재 등록되어 있는 상품들의 정보를 모두 조회한다.")
    @Test
    void getProductsTest() {
        // give
        when(productRepository.findAll()).thenReturn(productList());
        // when
        final List<ProductResponseDto> productList = productService.getProducts();
        // then
        assertThat(productList.size()).isEqualTo(5);
    }

    @DisplayName("DB에 현재 등록되어 있는 상품을 목록에서 조회한다.")
    @Test
    void searchProductsTest() {
        // give
        String query = "1";
        when(productRepository.findAllByTitleContains(query)).thenReturn(productList());

        // when
        final List<ProductResponseDto> productList = productService.searchProducts(query);
//
        // then
        assertThat(productList.size()).isEqualTo(1);
    }

    @DisplayName("DB에 현재 등록되어 있지 않은 상품을 목록에서 조회한다.")
    @Test
    void searchProductsTestFail() {
        // give
        String query = "7";
        when(productRepository.findAllByTitleContains(query)).thenThrow(new IllegalArgumentException(""));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.searchProducts(query);
        });
    }

    @DisplayName("DB에 등록되어 있는 상품의 상세 정보를 조회한다.")
    @Test
    void detailsProductTestSuccess() {
        // give
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(productList().get(0)));
        // when
        final ProductResponseDto product = productService.detailsProduct(productId);
        // then
        assertThat(product.getTitle()).isEqualTo("Test1");
    }

    @DisplayName("DB에 등록되어 있지 않은 상품의 상세 정보를 조회한다.")
    @Test
    void detailsProductTestFail() {
        // give
        Long productId = 1L;
        when(productRepository.findById(productId)).thenThrow(new IllegalArgumentException("해당 상품이 없습니다."));
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.detailsProduct(productId);
        });
    }
}