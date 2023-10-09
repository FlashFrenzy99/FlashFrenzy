package com.example.flashfrenzy.global.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Getter
@NoArgsConstructor
public class ItemDto {
    private String title;
    private String image;
    private Long price;
    private String category1; // 대분류  ex) 옷
    private String category2; // 중분류  ex) 하의, 상의
    private Long stock;

    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.image = itemJson.getString("image");
        this.price = (long) itemJson.getInt("lprice");
        this.category1 = itemJson.getString("category1");
        this.category2 = itemJson.getString("category2");
        this.stock = 100L;
    }
}