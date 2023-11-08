package com.example.flashfrenzy.domain.event.controller;

import com.example.flashfrenzy.domain.event.service.EventService;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/api/products/event-page")
    public String getEventPage(Model model) throws Exception {

        List<ProductResponseDto> productList = eventService.getEventProductList();
        model.addAttribute("productList", productList);
        return "event";
    }
}
