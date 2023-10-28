package com.example.flashfrenzy.domain.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockConcurrencyTest {

    @Autowired
    private OrderService orderService;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedissonClient redissonClient;



    @Test
    @DisplayName("동시에 100개의 주문을 하면 요청을 보내면 재고가 정확하게 남지 않는다.")
    public void 동시에_100개의요청() throws InterruptedException {

        //given

        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.orderBasketProducts(1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

//        Long stock = productRepository.findById(1L).get().getSto
//         assertThat(stock).isEqualTo(1750);
//        assertThat(stock).isEqualTo(0);
    }

}
