package com.example.flashfrenzy.domain.order.service;

import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.product.service.ProductService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderServiceFacade {
    private final RedissonClient redissonClient;
    private final OrderService orderService;


    public void orderBasketProductsFacade(Long id) {

        Long orderId;

        RLock lock = redissonClient.getLock("order");
        try {
            //락 획득 시도
            boolean available = lock.tryLock(20, 3, TimeUnit.SECONDS);

            //락 획득 실패
            if (!available) {
                log.error("주문 시도 중 lock 획득 실패");
                throw new InterruptedException("주문 요청이 많아 주문에 실패하였습니다.");
            }

            orderService.orderBasketProducts(id);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();

        }
    }

}
