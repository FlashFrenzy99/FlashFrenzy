package com.example.flashfrenzy.domain.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRankDto {

    private Long id;
    private String title;

    public ProductRankDto(Long id, String title) {

        this.id = id;
        this.title = title;
    }
}
