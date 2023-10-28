package com.example.flashfrenzy.domain.home.controller;

import com.example.flashfrenzy.domain.product.dto.ProductRankDto;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {
    private final ProductService productService;

    @GetMapping
    public String home(Model model, @PageableDefault(size = 15, sort = "id", direction = Direction.ASC) Pageable pageable) {
        long startTime = System.currentTimeMillis();
        List<ProductRankDto> productRankList = productService.getProductRank();
        Page<ProductResponseDto> productList = productService.getProducts(pageable);
        log.debug("홈화면 조회 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");

        model.addAttribute("productList", productList);
        model.addAttribute("productRankList", productRankList);
        return "product-list";
    }

    @GetMapping("/health")
    public ResponseEntity<HttpStatus> init() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
