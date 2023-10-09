package com.example.flashfrenzy.domain.basket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasketRequestForm {

    private Long productId;
    private Long count;

}
