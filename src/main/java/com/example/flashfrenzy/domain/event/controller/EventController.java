package com.example.flashfrenzy.domain.event.controller;

import com.example.flashfrenzy.domain.event.dto.EventCreateRequestDto;
import com.example.flashfrenzy.domain.event.service.EventService;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    //이벤트 발생
    @GetMapping("/api/products/event-page")
    public String getEventPage(Model model) throws Exception {

        List<ProductResponseDto> productList = eventService.getEventProductList();
        model.addAttribute("productList", productList);
        return "event";

    }
}
