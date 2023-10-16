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
import org.springframework.data.querydsl.QPageRequest;
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
        List<ProductResponseDto> productResponseDtoList =  productRepository.findAll().stream().map(ProductResponseDto::new).toList();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), productResponseDtoList.size());
        return new PageImpl<>(productResponseDtoList.subList(start,end), pageRequest, productResponseDtoList.size());
//        return productRepository.findAll().stream().map(ProductResponseDto::new).toList();
    }

    public Page<ProductResponseDto> searchProducts(String query, Pageable pageable) {
        log.debug("상품 검색");
        List<ProductResponseDto> productResponseDtoList = productRepository.findAllByTitleContains(query).stream().map(ProductResponseDto::new).toList();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), productResponseDtoList.size());

        //return productRepository.findAllByTitleContains(query).stream().map(ProductResponseDto::new).toList();
        return new PageImpl<>(productResponseDtoList.subList(start,end), pageRequest, productResponseDtoList.size());
    }


    public ProductResponseDto detailsProduct(Long productId) {
        log.debug("상품 상세 조회");
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        return new ProductResponseDto(product);
    }
}
