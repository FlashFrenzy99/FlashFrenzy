package com.example.flashfrenzy.domain.product.dto;

import com.example.flashfrenzy.domain.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;

    private String title;

    private String image;

    private Long price;

    private String category1; // 대분류  ex) 옷

    private String category2; // 중분류  ex) 하의, 상의

    private Long stock;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.category1 = product.getCategory1();
        this.category2 = product.getCategory2();
    }

    public ProductResponseDto(Product product, int saleRate) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.image = product.getImage();
        this.price = product.getPrice() * (100 - saleRate) / 100;
        this.category1 = product.getCategory1();
        this.category2 = product.getCategory2();
    }

    public ProductResponseDto(Product product, Long price, Long stock) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.image = product.getImage();
        this.price = price;
        this.category1 = product.getCategory1();
        this.category2 = product.getCategory2();
        this.stock = stock;
    }
}
