package com.example.flashfrenzy.domain.product.service;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getProducts() {
       return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    public List<ProductResponseDto> searchProducts(String query) {
        return productRepository.findAllByTitleContains(query).stream().map(ProductResponseDto::new).toList();
    }


    public ProductResponseDto detailsProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        return new ProductResponseDto(product);
    }
}
