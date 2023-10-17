package com.example.flashfrenzy.domain.product.entity;

import com.example.flashfrenzy.global.data.dto.ItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "category1", nullable = false)
    private String category1; // 대분류  ex) 옷

    @Column(name = "category2", nullable = false)
    private String category2; // 중분류  ex) 하의, 상의

    @Column(name = "stock", nullable = false)
    private Long stock;

    public Product(ItemDto itemDto) {
        this.title = itemDto.getTitle();
        this.image = itemDto.getImage();
        this.price = itemDto.getPrice();
        this.category1 = itemDto.getCategory1();
        this.category2 = itemDto.getCategory2();
        this.stock = itemDto.getStock();
    }

    public Product(Long id, String title, String image, Long price, String category1, String category2, Long stock) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.category1 = category1;
        this.category2 = category2;
        this.stock = stock;
    }

    public void discountStock(Long stock) {
        if (this.stock < stock) {
            throw new IllegalArgumentException("재고가 남아있지 않습니다. 남은재고: " + this.stock);
        }
        this.stock -= stock;
    }

    public void increaseStock() {
        this.stock = 100L;
    }
}
