package com.example.flashfrenzy.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EventCreateRequestDto {

    private Long productId;
    private int saleRate;
}
