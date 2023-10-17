package com.example.flashfrenzy.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.product.service.ProductService;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketProductRepository basketProductRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("장바구니에 담은 물품이 없으면 주문을 생성할 수 없다.")
    void OrderWithoutProduct() {
        //given
        User user = new User("test", "123", UserRoleEnum.USER, "hello@naver.com", new Basket());

        User savedUser = userRepository.save(user);

        //when - then
        Assertions.assertThatThrownBy(() ->
                        orderService.orderBasketProducts(savedUser.getBasket().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("장바구니에 물품이 1개 이상 존재해야 주문이 가능합니다.");
    }

    @Test
    @DisplayName("장바구니에 담은 물품들로 주문을 생성한다.")
    public void createOrder() throws Exception {
        //given
        User user = new User("test", "123", UserRoleEnum.USER, "hello@naver.com", new Basket());
        User savedUser = userRepository.save(user);
        Basket basket = savedUser.getBasket();

        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        BasketProduct basketProduct1 = new BasketProduct(2L, basket, product1);
        BasketProduct basketProduct2 = new BasketProduct(5L, basket, product2);

        basket.getList().add(basketProduct1);
        basket.getList().add(basketProduct2);

        basketProductRepository.save(basketProduct1);
        basketProductRepository.save(basketProduct2);

        //when
        Long orderId = orderService.orderBasketProducts(basket.getId());
        Order findOrder = orderRepository.findById(orderId).get();

        //then
        assertThat(findOrder.getId()).isNotNull();
        assertThat(findOrder.getUser()).isEqualTo(savedUser);

        assertThat(findOrder.getOrderProductList()).hasSize(2)
                .extracting("order", "product", "count")
                .containsExactlyInAnyOrder(
                        tuple(findOrder, product1, 2L),
                        tuple(findOrder, product2, 5L)
                );
    }

    @Test
    @DisplayName("주문한 상품의 개수만큼 상품의 재고가 감소한다.")
    public void createOrderStockChecking() throws Exception {
        //given
        User user = new User("test", "123", UserRoleEnum.USER, "hello@naver.com", new Basket());
        User savedUser = userRepository.save(user);
        Basket basket = savedUser.getBasket();

        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        Long productStock1 = product1.getStock();
        Long productStock2 = product2.getStock();

        BasketProduct basketProduct1 = new BasketProduct(2L, basket, product1);
        BasketProduct basketProduct2 = new BasketProduct(5L, basket, product2);

        basket.getList().add(basketProduct1);
        basket.getList().add(basketProduct2);

        basketProductRepository.save(basketProduct1);
        basketProductRepository.save(basketProduct2);
        //when
        Long orderId = orderService.orderBasketProducts(basket.getId());

        //then
        assertThat(product1.getStock()).isEqualTo(productStock1 - 2L);
        assertThat(product2.getStock()).isEqualTo(productStock2 - 5L);
    }

    @Test
    @DisplayName("주문하려는 상품의 개수가 재고보다 많으면 주문을 생성할 수 없다.")
    public void createOrderWithNoStock() throws Exception {
        //given
        User user = new User("test", "123", UserRoleEnum.USER, "hello@naver.com", new Basket());
        User savedUser = userRepository.save(user);
        Basket basket = savedUser.getBasket();

        Product product1 = productRepository.findById(1L).get();
        Product product2 = productRepository.findById(2L).get();

        Long productStock1 = product1.getStock();
        Long productStock2 = product2.getStock();

        BasketProduct basketProduct1 = new BasketProduct(productStock1 + 1, basket, product1);
        BasketProduct basketProduct2 = new BasketProduct(productStock2 + 1, basket, product2);

        basket.getList().add(basketProduct1);
        basket.getList().add(basketProduct2);

        basketProductRepository.save(basketProduct1);
        basketProductRepository.save(basketProduct2);

        //when - then
        Assertions.assertThatThrownBy(() -> orderService.orderBasketProducts(basket.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문하려는 물품의 재고가 부족합니다. name: " + product1.getTitle());
    }

}