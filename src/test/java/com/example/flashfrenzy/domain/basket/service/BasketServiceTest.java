package com.example.flashfrenzy.domain.basket.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.dto.BasketProductResponseDto;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.NotExtensible;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    BasketRepository basketRepository;

    @Mock
    BasketProductRepository basketProductRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    BasketService basketService;


    @BeforeEach
    void setUp() {

    }

    @Nested
    @DisplayName("장바구니 상품 조회 테스트")
    class GetBasket {

        @Mock
        User user;
        @Mock
        Basket basket;
        @Mock
        BasketProduct basketProduct;
        @Mock
        Product product;

        @Test
        @DisplayName("장바구니 상품 조회 성공")
        void getBasket_success() {
            //getBasket(User user)의 user 의 정보만으로는 장바구니의 정보를 가져올 수 없음
        }

        @Test
        @DisplayName("장바구니 상품 조회 실패")
        void getBasket_fail() {
            //given
            //when
            when(user.getBasket()).thenReturn(basket);
            when(basketRepository.findById(basket.getId())).thenReturn(Optional.empty());

            //then
            assertThrows(IllegalArgumentException.class, () -> {
                basketService.getBasket(user);
            });
        }
    }

    @Nested
    @DisplayName("장바구니 삭제 테스트")
    class DeleteBasket {

        @Mock
        User user;
        @Mock
        Basket basket;
        @Mock
        BasketProduct basketProduct;
        @Mock
        Product product;

        @Test
        @DisplayName("장바구니 내부에 상품 x")
        void deleteBasket_fail() {
            //given
            //when
            when(basketProductRepository.findById(anyLong())).thenReturn(Optional.empty());
            //then
            assertThrows(IllegalArgumentException.class, () -> {
                basketService.deleteBasket(anyLong());
            });
        }

    }

    @Nested
    @DisplayName("장바구니 담기 테스트")
    class AddBasket {

        @Mock
        User user;
        @Mock
        Basket basket;
        @Mock
        BasketProduct basketProduct;
        @Mock
        Product product;

        @BeforeEach
        void setUp() {

        }

        @Test
        @DisplayName("상품을 담을 장바구니를 찾을 수 없다")
        void addBasket_fail1() {
            //given
            BasketRequestForm requestForm = new BasketRequestForm();
            //when
            when(basketRepository.findById(anyLong())).thenReturn(Optional.empty());
            //then
            assertThrows(IllegalArgumentException.class, () -> {
                basketService.addBasket(anyLong(), requestForm);
            });
        }
        @Test
        @DisplayName("장바구니에 담을 해당 상품을 찾을 수 없다.")
        void addBasket_fail2() {
            //given
            Long productId = 1L;
            Long count = 2L;
            BasketRequestForm requestForm = new BasketRequestForm(productId, count);
            //when
            when(productRepository.findById(requestForm.getProductId())).thenReturn(Optional.empty());
            when(basketRepository.findById(anyLong())).thenReturn(Optional.of(basket));
            //then
            assertThatThrownBy(() -> basketService.addBasket(anyLong(), requestForm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 상품을 찾을 수 없습니다.");

        }
        @Test
        @DisplayName("장바구니에 해당 물품이 존재한다면 수량만 더해주기")
        void deleteBasket_success1() {
            //given
            BasketProduct duplicatedBasketProduct = new BasketProduct(10L, basket,product);
            Long productId = 1L;
            Long count = 2L;
            BasketRequestForm requestForm = new BasketRequestForm(productId, count);
            //when
            when(basketRepository.findById(anyLong())).thenReturn(Optional.of(basket));
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            when(basketProductRepository.findByBasketAndProduct(basket,product)).thenReturn(Optional.of(duplicatedBasketProduct));

            basketService.addBasket(anyLong(), requestForm);
            //then
            assertEquals(duplicatedBasketProduct.getCount(), 10 + requestForm.getCount());
        }

        @Test
        @DisplayName("장바구니에 해당 상품이 없다면 넣어주기 ")
        void deleteBasket_success2() {
            //given
            Long productId = 1L;
            Long count = 2L;
            BasketRequestForm requestForm = new BasketRequestForm(productId, count);
            //when
            when(basketRepository.findById(anyLong())).thenReturn(Optional.of(basket));
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            when(basketProductRepository.findByBasketAndProduct(basket,product)).thenReturn(Optional.empty());

            basketService.addBasket(anyLong(), requestForm);
            //then
            verify(basketProductRepository, times(1)).save(any(BasketProduct.class));

        }


    }


}