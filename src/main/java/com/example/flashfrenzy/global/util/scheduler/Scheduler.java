package com.example.flashfrenzy.global.util.scheduler;

import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.orderProduct.repository.OrderProductRepository;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    // 초, 분, 시, 일, 주, 월 순서
    @Scheduled(cron = "*/30 * * * * *") // 매일 자정
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
    }
}