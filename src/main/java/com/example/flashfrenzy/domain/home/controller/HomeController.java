package com.example.flashfrenzy.domain.home.controller;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {
    private final ProductService productService;

    @GetMapping
    public String home(Model model, @PageableDefault(size = 15, page = 1, sort = "title", direction = Sort.Direction.DESC) Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<ProductResponseDto> productList = productService.getProducts(pageable);
        log.info("홈화면 조회 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("productList", productList);
        return "product-list";
    }
}
