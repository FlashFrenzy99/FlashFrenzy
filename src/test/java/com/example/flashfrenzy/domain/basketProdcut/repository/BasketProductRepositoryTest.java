package com.example.flashfrenzy.domain.basketProdcut.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BasketProductRepositoryTest {

    @Autowired
    private BasketProductRepository basketProductRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("유저의 장바구니 모두 비우기")
    @Test
    void emptyBasket(){
        //given
        Long productCount = productRepository.count();
        Product product = new Product(productCount + 1, "title","image",1000L,"category1","category2", 10L);
        productRepository.save(product);

        List<BasketProduct> list = new ArrayList<>();
        Basket basket = new Basket();
        Long basketCount = basketRepository.count();
        basket.setId(basketCount + 1);
        basket.setList(list);

        BasketProduct basketProduct = new BasketProduct(2L, basket, product);
        basket.getList().add(basketProduct);

        basketRepository.save(basket);
        basketProductRepository.save(basketProduct);

        Long basketProductCount = basketProductRepository.count();
        //when
        basketProductRepository.deleteAllByBasket(basket);

        //then
        //null 일때는 테스트 성공
        //삭제 된 상태에선 get으로 접근이 안되는 듯함
        assertThat(basketProductRepository.count()).isEqualTo(basketProductCount - 1);
    }

    @DisplayName("장바구니 안에 해당상품이 가져오기 성공")
    @Test
    void getInBasketOfProduct(){

        //given
        Long productCount = productRepository.count();
        Product product = new Product(productCount, "title","image",1000L,"category1","category2", 10L);
        productRepository.save(product);

        List<BasketProduct> list = new ArrayList<>();
        Basket basket = new Basket();
        Long basketCount = basketRepository.count();
        basket.setId(basketCount);
        basket.setList(list);

        BasketProduct basketProduct = new BasketProduct(2L, basket, product);
        basket.getList().add(basketProduct);

        basketRepository.save(basket);
        basketProductRepository.save(basketProduct);

        //when
        //test 전용 db를 만들어서 테스트 해야한다.
        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findByBasketAndProduct(basket, product);

        //then
        assertThat(optionalBasketProduct.get().getProduct().getId()).isEqualTo(productCount);
        assertThat(optionalBasketProduct.get().getBasket().getId()).isEqualTo(basketCount);
    }

}