package com.example.flashfrenzy.domain.order.controller;

import com.example.flashfrenzy.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping("/{id}")
    @ResponseBody
    public String orderBasketProducts(@PathVariable(value = "id") Long basketId) {
        long startTime = System.currentTimeMillis();
        orderService.orderBasketProducts(basketId);
        log.debug("장바구니 주문 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        return "hello";
        //return "redirect:/api/baskets";
    }



}
