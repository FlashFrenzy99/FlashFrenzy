package com.example.flashfrenzy.global.util.scheduler;

import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.orderProduct.repository.OrderProductRepository;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.global.redis.RedisRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final EventRepository eventRepository;
    private final RedisRepository redisRepository;

    // 초, 분, 시, 일, 주, 월 순서
    //@Scheduled(cron = "*/30 * * * * *") // 매일 자정
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void putStock() {
        List<Product> products = orderProductRepository.findAllWithProduct().stream().map(
                OrderProduct::getProduct).toList();

        HashSet<Product> productList = new HashSet<>(products);

        for (Product product : productList) {
            System.out.println("productId" + product.getId());
            product.increaseStock();
        }
    }

    // 초, 분, 시, 일, 주, 월 순서
    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시
    public void updateEvent() {
        //오전 9시 마다 이벤트 일괄 삭제 및 세일 상품 등록(20개)
        long startTime = System.currentTimeMillis();

        eventRepository.deleteAll();

        String productTotal = redisRepository.getValue("product:total");
        Long total;
        if (productTotal == null) {
            total = productRepository.count();
            redisRepository.save("product:total", String.valueOf(total));
        } else {
            total = Long.valueOf(productTotal);
        }

        Set<Long> randomNumbers = new HashSet<>();
        Random random = new Random();

        while (randomNumbers.size() < 20) {
            Long randomNumber = random.nextLong(total) + 1;
            randomNumbers.add(randomNumber);
        }

        List<Long> idList = randomNumbers.stream().toList();

        List<Event> eventList = productRepository.findByIdIn(idList).stream().map(
                product -> {
                    int rate = 10 + (int) (Math.random() * 7) * 5;
                    return new Event(product, rate);
                }
        ).toList();

        eventRepository.saveAll(eventList);

        log.info("스케줄 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");

    }
}