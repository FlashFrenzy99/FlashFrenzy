package com.example.flashfrenzy.domain.home.controller;

import com.example.flashfrenzy.domain.product.controller.ProductController;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class HomeController {
    private final ProductController productController;
    @GetMapping
    public String home(Model model, Pageable pageable) {
        return productController.getProducts(model, pageable);
    }
}
