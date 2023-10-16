package com.example.flashfrenzy.domain.product.service;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "Product API")
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        log.debug("상품 조회");
        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    public List<ProductResponseDto> searchProducts(String query) {
        log.debug("상품 검색");
        return productRepository.findAllByTitleContains(query).stream().map(ProductResponseDto::new).toList();
    }


    public ProductResponseDto detailsProduct(Long productId) {
        log.debug("상품 상세 조회");
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        return new ProductResponseDto(product);
    }
}
