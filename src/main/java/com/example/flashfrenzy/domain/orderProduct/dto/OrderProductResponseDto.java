package com.example.flashfrenzy.domain.orderProduct.dto;

import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderProductResponseDto {

    private Long id;
    private String title;
    private Long count;

    public OrderProductResponseDto(OrderProduct orderProduct) {
        this.id = orderProduct.getId();
        this.title = orderProduct.getProduct().getTitle();
        this.count = orderProduct.getCount();
    }
}
