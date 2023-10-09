package com.example.flashfrenzy.domain.basket.dto;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasketResponseDto {

    private List<BasketProduct> list = new ArrayList<>();

    public void setBasket(BasketProduct basket){
        list.add(basket);
    }
}
