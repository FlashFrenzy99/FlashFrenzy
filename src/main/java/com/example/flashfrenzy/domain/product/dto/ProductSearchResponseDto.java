package com.example.flashfrenzy.domain.product.dto;

public class ProductSearchResponseDto {
    private Long product_id;
    private String title;
    private String image;
    private Long price;
    private String category1; // 대분류  ex) 옷
    private String category2; // 중분류  ex) 하의, 상의
}

