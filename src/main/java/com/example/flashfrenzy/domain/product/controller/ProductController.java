package com.example.flashfrenzy.domain.product.controller;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String getProducts(Model model,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
            throws JsonProcessingException {
        long startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productList = productService.getProducts(pageable);
        log.debug("상품 조회 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        return "product-list";
    }

    @GetMapping("/search")
    public String searchProducts(Model model, @RequestParam(value = "query") String query,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productList = productService.searchProducts(query, pageable);
        log.debug("상품 검색 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("query", query);
        return "product-list";
    }

    @GetMapping("/{id}")
    public String detailsProduct(Model model, @PathVariable(value = "id") Long productId) {
        ProductResponseDto product = productService.detailsProduct(productId);
        model.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/category")
    public String categoryProduct(Model model, @RequestParam String cate,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable)
            throws JsonProcessingException {
        long startTime = System.currentTimeMillis();

        Page<ProductResponseDto> productList = productService.categoryProduct(cate, pageable);
        model.addAttribute("productList", productList);
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("cate", cate);

        log.debug("카테고리 검색 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
        return "product-list";
    }
}