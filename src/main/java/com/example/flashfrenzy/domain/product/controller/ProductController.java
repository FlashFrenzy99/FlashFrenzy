package com.example.flashfrenzy.domain.product.controller;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 리스트 조회
     */
    @GetMapping
    public String getProducts(Model model) {
        List<ProductResponseDto> productList = productService.getProducts();
        model.addAttribute("productList", productList);
        return "product-list";
    }

    /**
     * 상품 검색
     */
    @GetMapping("/search")
    public String searchProducts(Model model, @RequestParam(value = "query") String query) {
        List<ProductResponseDto> productList = productService.searchProducts(query);
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
}
