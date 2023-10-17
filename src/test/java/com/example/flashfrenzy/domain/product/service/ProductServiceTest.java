package com.example.flashfrenzy.domain.product.service;

import com.example.flashfrenzy.domain.event.repository.EventRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
    @Mock
    private EventRepository eventRepository;

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
        when(productRepository.findTop2000By()).thenReturn(productList());
        // when
        final Page<ProductResponseDto> productList = productService.getProducts();
        // then
        assertThat(productList.getSize()).isEqualTo(5);
    }

    @DisplayName("DB에 현재 등록되어 있는 상품을 목록에서 조회한다.")
    @Test
    void searchProductsTest() {
        // give
        String query = "1";
        List<Product> dummy = new ArrayList<>();
        dummy.add(productList().get(0));        // 더미 값을 넣어주기 위해 만든 리스트
        when(productRepository.findAllByTitleContains(query)).thenReturn(dummy);
        // when
        final Page<ProductResponseDto> productList = productService.searchProducts(query, pageable);

        // then
        assertThat(productList.getTotalElements()).isEqualTo(1);
    }

    @DisplayName("DB에 현재 등록되어 있지 않은 상품을 목록에서 조회한다.")
    @Test
    void searchProductsTestFail() {
        // give
        String query = "7";
        when(productRepository.findAllByTitleContains(query)).thenReturn(List.of());

        // when
        final Page<ProductResponseDto> productList = productService.searchProducts(query, pageable);
        // then
        assertThat(productList.getTotalElements()).isEqualTo(0);
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

    private final Pageable pageable =new Pageable() {
        @Override
        public int getPageNumber() {
            return 0;
        }

        @Override
        public int getPageSize() {
            return 5;
        }

        @Override
        public long getOffset() {
            return 0;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public Pageable next() {
            return null;
        }

        @Override
        public Pageable previousOrFirst() {
            return null;
        }

        @Override
        public Pageable first() {
            return null;
        }

        @Override
        public Pageable withPage(int pageNumber) {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }
    };
}