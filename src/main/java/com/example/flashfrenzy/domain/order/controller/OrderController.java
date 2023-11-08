package com.example.flashfrenzy.domain.order.controller;

import com.example.flashfrenzy.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{id}")
    public String orderBasketProducts(@PathVariable(value = "id") Long basketId) {
        long startTime = System.currentTimeMillis();
        orderService.orderBasketProducts(basketId);
        log.debug("장바구니 주문 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
        return "redirect:/auth/users/my-page";
    }
}
