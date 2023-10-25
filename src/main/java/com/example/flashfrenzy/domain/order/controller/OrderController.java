package com.example.flashfrenzy.domain.order.controller;

import com.example.flashfrenzy.domain.order.service.OrderServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderServiceFacade orderServiceFacade;
    @PostMapping("/{id}")
    public String orderBasketProducts(@PathVariable(value = "id") Long basketId) {
        long startTime = System.currentTimeMillis();
        orderServiceFacade.orderBasketProductsFacade(basketId);
        log.info("장바구니 주문 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        //return "hello";
        return "redirect:/auth/users/my-page";
    }



}