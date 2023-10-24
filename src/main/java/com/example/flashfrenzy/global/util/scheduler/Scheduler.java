package com.example.flashfrenzy.global.util.scheduler;

import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.orderProduct.repository.OrderProductRepository;
import com.example.flashfrenzy.domain.product.dto.ProductRankDto;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final ObjectMapper objectMapper;

    // 초, 분, 시, 일, 주, 월 순서
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    @Transactional
    public void initProducts() throws JsonProcessingException {

        //재고 채우기
        List<Product> products = orderProductRepository.findAllWithZeroStock().stream().map(
                OrderProduct::getProduct).toList();

        HashSet<Product> productList = new HashSet<>(products);

        for (Product product : productList) {
            System.out.println("productId" + product.getId());
            product.increaseStock();
        }

        //인기상품 TOP 5
        List<Product> orderProducts = orderProductRepository.findTop5Order();

        int i = 1;
        for (Product product : orderProducts) {
            ProductRankDto productRankDto = new ProductRankDto(product.getId(), product.getTitle());
            String productRankString = objectMapper.writeValueAsString(productRankDto);
            redisRepository.save("product:rank:" + i, productRankString);

            String value = redisRepository.getValue("product:rank:" + i);
            i++;
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
        Integer i = 1;
        List<Long> idList = randomNumbers.stream().toList();
        List<Product> eventProductList = productRepository.findByIdIn(idList);

        AtomicInteger index = new AtomicInteger(1);
        List<Event> eventList = eventProductList.stream().map(
                product -> {
                    //레디스에 현재 재고 저장
                    int currentIndex = index.getAndIncrement();
                    int rate = 10 + (int) (Math.random() * 7) * 5; // 10 ~ 40

                    try {
                        String productString = objectMapper.writeValueAsString(product);
                        redisRepository.save("product:sale:" + currentIndex + ":stock",
                                String.valueOf(product.getStock()));
                        redisRepository.save("product:sale:"+ currentIndex + ":price",
                                String.valueOf(product.getPrice()*(100 - rate)/100));
                        redisRepository.save("product:sale:" + currentIndex, productString);
                    } catch (JsonProcessingException e) {
                        log.error("상품을 스트링으로 변환하는 도중 오류가 발생하였습니다.");
                        throw new RuntimeException(e);
                    }
                    return new Event(product, rate);
                }
        ).toList();

        eventRepository.saveAll(eventList);

        log.info("스케줄 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
    }

    // 초, 분, 시, 일, 주, 월 순서
    @Scheduled(cron = "*/10 * * * * *") // 매시간 정각
    public void updateEventStock() {
        // event -> product -> stock cache update

        List<Event> events = eventRepository.findEventProductList();
        for (int i = 1; i <= 20; i++) {
            Long stock = events.get(i - 1).getProduct().getStock();
            redisRepository.save("product:sale:" + i + ":stock", String.valueOf(stock));
        }

    }
}