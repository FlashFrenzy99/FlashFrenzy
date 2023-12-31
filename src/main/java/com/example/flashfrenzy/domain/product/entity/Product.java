
package com.example.flashfrenzy.domain.product.entity;

import com.example.flashfrenzy.global.data.dto.ItemDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Entity
@Getter
@NoArgsConstructor
@Document(indexName = "product")
@Table(name = "product", indexes = @Index(name = "idx_category1", columnList = "category1"))
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Field(name = "product_id")
    @org.springframework.data.annotation.Id
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

    public Product(ItemDto itemDto) {
        this.title = itemDto.getTitle();
        this.image = itemDto.getImage();
        this.price = itemDto.getPrice();
        this.category1 = itemDto.getCategory1();
        this.category2 = itemDto.getCategory2();
    }

    public Product(Long id, String title, String image, Long price, String category1,
            String category2, Long stock) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
        this.category1 = category1;
        this.category2 = category2;
    }

}
