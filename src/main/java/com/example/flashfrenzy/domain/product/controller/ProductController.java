package com.example.flashfrenzy.domain.product.controller;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import com.example.flashfrenzy.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    /**
     * 상품 리스트 조회
     */
    @GetMapping
    public String getProducts(Model model,@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productList = productService.getProducts(pageable);
        log.info("상품 조회 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("productList", productList);
        return "product-list";
    }

    /**
     * 상품 검색
     */
    @GetMapping("/search")
    public String searchProducts(Model model, @RequestParam(value = "query") String query,
                                 @PageableDefault(size = 15,sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productList = productService.searchProducts(query, pageable);
        log.info("상품 검색 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("productList", productList);
        return "product-list";
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("/{id}")
    public String detailsProduct(Model model, @PathVariable(value = "id") Long productId){
        ProductResponseDto product = productService.detailsProduct(productId);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/category")
    public String categoryProduct(Model model, @RequestParam String cate,
                                  @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        long startTime = System.currentTimeMillis();

        
            Page<ProductResponseDto> productList = productService.categoryProduct(cate, pageable);
            model.addAttribute("productList", productList);


        log.info("카테고리 검색 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        return "product-list";
    }
}
