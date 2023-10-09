package com.example.flashfrenzy.domain.order.dto;

import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.orderProduct.dto.OrderProductResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;
    private List<OrderProductResponseDto> orderProductList = new ArrayList<>();

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderProductList = order.getOrderProductList().stream().map(OrderProductResponseDto::new).toList();
    }
}