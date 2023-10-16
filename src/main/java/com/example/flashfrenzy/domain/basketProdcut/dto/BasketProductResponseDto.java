package com.example.flashfrenzy.domain.basketProdcut.dto;

import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import lombok.Getter;

@Getter
public class
BasketProductResponseDto {

    private Long BasketProductId;

    private String title;
    private String image;
    private Long price;
    private String category1;
    private String category2;
    private Long stock;


    public BasketProductResponseDto(BasketProduct basketProduct){
        this.BasketProductId = basketProduct.getId();
        this.title = basketProduct.getProduct().getTitle();
        this.image = basketProduct.getProduct().getImage();
        this.price = basketProduct.getProduct().getPrice();
        this.category1 = basketProduct.getProduct().getCategory1();
        this.category2 = basketProduct.getProduct().getCategory2();
        this.stock = basketProduct.getCount();
    }
}
