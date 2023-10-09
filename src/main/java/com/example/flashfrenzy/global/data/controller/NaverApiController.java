package com.example.flashfrenzy.global.data.controller;

import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.global.data.service.NaverApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NaverApiController {

    private final NaverApiService naverApiService;
    private final ProductRepository productRepository;

    @GetMapping("/search")
    public void searchItems(@RequestParam String query, @RequestParam String page)  {
        naverApiService.searchItems(query, page, productRepository);
    }
}