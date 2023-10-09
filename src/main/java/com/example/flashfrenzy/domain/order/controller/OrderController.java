package com.example.flashfrenzy.domain.order.controller;

import com.example.flashfrenzy.domain.order.service.OrderService;
import com.example.flashfrenzy.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;


    @PostMapping("/{id}")
    public String orderBasketProducts(@PathVariable(value = "id") Long basketId) {
        orderService.orderBasketProducts(basketId);

//        장바구니 id 로 userid 받아오기
        return "redirect:/auth/users/my-page";
    }

}