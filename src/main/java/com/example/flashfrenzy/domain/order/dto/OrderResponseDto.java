package com.example.flashfrenzy.domain.order.dto;

import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.orderProduct.dto.OrderProductResponseDto;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.orderProduct.entity.StatusEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;
    private List<OrderProductResponseDto> orderProductList = new ArrayList<>();
    private Long totalPrice;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.orderProductList = order.getOrderProductList().stream()
                .map(OrderProductResponseDto::new).toList();
        this.totalPrice = 0L;
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            if (orderProduct.getStatus() == StatusEnum.SUCCESS) {
                this.totalPrice += orderProduct.getPrice() * orderProduct.getCount();
            }
        }
    }
}