package com.example.flashfrenzy.domain.home.controller;

import com.example.flashfrenzy.domain.product.controller.ProductController;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ProductService productService;

    @GetMapping
    public String  home(Model model,@PageableDefault(size = 15) Pageable pageable) {
        Page<ProductResponseDto> productList = productService.getProducts(pageable);
        model.addAttribute("productList", productList);

        return "product-list";
    }
}
