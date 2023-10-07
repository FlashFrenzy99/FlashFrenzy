package com.example.flashfrenzy.domain.order.controller;

import com.example.flashfrenzy.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

}
