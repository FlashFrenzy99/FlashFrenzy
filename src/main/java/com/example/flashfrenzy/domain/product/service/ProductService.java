package com.example.flashfrenzy.domain.product.service;

import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.product.dto.ProductRankDto;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.product.repository.ProductSearchRepository;
import com.example.flashfrenzy.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Product API")
public class ProductService {

    private final ProductRepository productRepository;
    private final EventRepository eventRepository;
    private final ProductSearchRepository productSearchRepository;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(Pageable pageable) throws JsonProcessingException {
        log.debug("상품 조회");

        int pageNum = pageable.getPageNumber();
        String value = redisRepository.getValue("main:" + pageNum);

        if (pageNum < 15) {
            if (value == null) {
                Set<Long> eventIdList = eventRepository.findProductIdSet();
                Stream<Product> list = productRepository.streamAllPaged(pageable);

                List<ProductResponseDto> productResponseDtoList = list.map(product -> {
                    if (eventIdList.contains(product.getId())) {
                        Event event = eventRepository.findById(product.getId()).orElseThrow();
                        return new ProductResponseDto(product, event.getSaleRate());
                    } else {
                        return new ProductResponseDto(product);
                    }
                }).toList();

                value = objectMapper.writeValueAsString(productResponseDtoList);
                redisRepository.save("main:" + pageNum, value);
                return new PageImpl<>(productResponseDtoList);
            } else {
                return new PageImpl<>(
                        Arrays.asList(objectMapper.readValue(value, ProductResponseDto[].class)));
            }
        }

        Set<Long> eventIdList = eventRepository.findProductIdSet();

        Stream<Product> list = productRepository.streamAllPaged(pageable);
        List<ProductResponseDto> productResponseDtoList = list.map(product -> {
            if (eventIdList.contains(product.getId())) {
                Event event = eventRepository.findById(product.getId()).orElseThrow();
                return new ProductResponseDto(product, event.getSaleRate());
            } else {
                return new ProductResponseDto(product);
            }
        }).toList();

        return new PageImpl<>(productResponseDtoList);
    }

    public Page<ProductResponseDto> searchProducts(String query, Pageable pageable) {
        log.debug("상품 검색");
        long startTime = System.currentTimeMillis();

        startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productResponseDtoList = productSearchRepository.searchByTitle(
                query, pageable).map(ProductResponseDto::new);
        log.debug("엘라스틱 서치 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");

        return productResponseDtoList;
    }


    public ProductResponseDto detailsProduct(Long productId) {
        log.debug("상품 상세 조회");
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        return new ProductResponseDto(product);
    }

    public Page<ProductResponseDto> categoryProduct(String cate, Pageable pageable)
            throws JsonProcessingException {

        int pageNum = pageable.getPageNumber();
        String value = redisRepository.getValue("category:" + cate + ":pagenum:" + pageNum);

        if (pageNum < 15) {
            if (value == null) {
                Set<Long> eventIdList = eventRepository.findProductIdSet();
                Page<Product> list = productRepository.findAllByCategory1(cate, pageable);
                List<ProductResponseDto> productResponseDtoList = list.stream().map(product -> {
                    if (eventIdList.contains(product.getId())) {
                        Event event = eventRepository.findById(product.getId()).orElseThrow();
                        return new ProductResponseDto(product, event.getSaleRate());
                    } else {
                        return new ProductResponseDto(product);
                    }
                }).toList();
                value = objectMapper.writeValueAsString(productResponseDtoList);
                redisRepository.save("category:" + cate + ":pagenum:" + pageNum, value);
                return new PageImpl<>(productResponseDtoList, pageable, list.getTotalElements());
            } else {
                Page<ProductResponseDto> productList = new PageImpl<>(
                        Arrays.asList(objectMapper.readValue(value, ProductResponseDto[].class)));
                return productList;
            }
        }

        Set<Long> eventIdList = eventRepository.findProductIdSet();
        Page<Product> list = productRepository.findAllByCategory1(cate, pageable);
        List<ProductResponseDto> productResponseDtoList = list.stream().map(product -> {
            if (eventIdList.contains(product.getId())) {
                Event event = eventRepository.findById(product.getId()).orElseThrow();
                return new ProductResponseDto(product, event.getSaleRate());
            } else {
                return new ProductResponseDto(product);
            }
        }).toList();

        return new PageImpl<>(productResponseDtoList, pageable, list.getTotalElements());

    }

    public List<ProductRankDto> getProductRank() {

        List<ProductRankDto> productRankList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String value = redisRepository.getValue("product:rank:" + i);
            if (value != null) {
                try {
                    ProductRankDto productRankDto = objectMapper.readValue(value,
                            ProductRankDto.class);
                    productRankList.add(productRankDto);
                } catch (JsonProcessingException e) {
                    log.error("상품 랭킹 변환 과정에서 에러가 발생하였습니다.");
                    throw new RuntimeException(e);
                }
            }
        }
        return productRankList;
    }
}
