package com.example.flashfrenzy.domain.product.service;

import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.product.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "Product API")
public class ProductService {

    private final ProductRepository productRepository;
    private final EventRepository eventRepository;
    private final ProductSearchRepository productSearchRepository;

    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        log.debug("상품 조회");

        /*개선 이전*/
//        List<Product> list = productRepository.findTop2000By();
//        List<ProductResponseDto> productResponseDtoList = list.stream().map(product -> {
//            Optional<Event> eventOptional = eventRepository.findById(product.getId());
//            if (eventOptional.isPresent()) {
//                Event event = eventOptional.get();
//                return new ProductResponseDto(product, event.getSaleRate());
//            } else {
//                return new ProductResponseDto(product);
//            }
//        }).toList();


        List<Long> eventIdList = eventRepository.findProductIdList();

        Stream<Product> list = productRepository.streamAllPaged(pageable);
//        List<Product> list = productRepository.findTop2000By(pageable);
        List<ProductResponseDto> productResponseDtoList = list.map(product -> {
            if (eventIdList.contains(product.getId())) {
                Event event = eventRepository.findById(product.getId()).orElseThrow();
                return new ProductResponseDto(product, event.getSaleRate());
            } else {
                return new ProductResponseDto(product);
            }
        }).toList();
//        list.close();

//        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

//        int start = (int) pageRequest.getOffset();
//        int end = Math.min((start + pageRequest.getPageSize()), productResponseDtoList.size());

        return new PageImpl<>(productResponseDtoList);
//        return new PageImpl<>(productResponseDtoList.subList(start,end), pageRequest, productResponseDtoList.size());
    }

    public Page<ProductResponseDto> searchProducts(String query, Pageable pageable) {
        log.debug("상품 검색");
//        Stream<Product> productList = productRepository.findAllByTitleContainsAndStream(query, pageable);
//        List<ProductResponseDto> productResponseDtoList = productList.map(ProductResponseDto::new).toList();
        long startTime = System.currentTimeMillis();

        List<ProductResponseDto> productResponseDtoList = productRepository.findAllByCustomQueryAndStream(query, pageable).map(ProductResponseDto::new).toList();
        log.info("기존 서치 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");

       // startTime = System.currentTimeMillis();
        //List<ProductResponseDto> productResponseDtoList2 = productSearchRepository.searchByTitle(query, pageable).map(ProductResponseDto::new).toList();
        //log.info("엘라스틱 서치 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");

//        List<ProductResponseDto> productResponseDtoList = productRepository.findAllByTitleContains(query).stream().map(ProductResponseDto::new).toList();

//        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
//        int start = (int) pageRequest.getOffset();
//        int end = Math.min((start + pageRequest.getPageSize()), productResponseDtoList.size());

//        return new PageImpl<>(productResponseDtoList.subList(start,end), pageRequest, productResponseDtoList.size());
        return new PageImpl<>(productResponseDtoList);
    }


    public ProductResponseDto detailsProduct(Long productId) {
        log.debug("상품 상세 조회");
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        return new ProductResponseDto(product);
    }

    public Page<ProductResponseDto> categoryProduct(String cate, Pageable pageable) {
        List<Long> eventIdList = eventRepository.findProductIdList();

//        List<Product> list = productRepository.findAllByCategory1(cate);
        Page<Product> list = productRepository.findAllByCategory1(cate, pageable);
        List<ProductResponseDto> productResponseDtoList = list.stream().map(product -> {
            if (eventIdList.contains(product.getId())) {
                Event event = eventRepository.findById(product.getId()).orElseThrow();
                return new ProductResponseDto(product, event.getSaleRate());
            } else {
                return new ProductResponseDto(product);
            }
        }).toList();

//        Pageable pageRequest = PageRequest.of(0, 15);

//        int start = (int) pageRequest.getOffset();
//        int end = Math.min((start + pageRequest.getPageSize()), productResponseDtoList.size());

//        return new PageImpl<>(productResponseDtoList.subList(start,end), pageRequest, productResponseDtoList.size());
        return new PageImpl<>(productResponseDtoList, pageable, list.getTotalElements());
    }

}
