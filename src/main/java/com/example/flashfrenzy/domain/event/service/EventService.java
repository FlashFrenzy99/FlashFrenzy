package com.example.flashfrenzy.domain.event.service;

import com.example.flashfrenzy.domain.event.dto.EventCreateRequestDto;
import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "Event API")
public class EventService {

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createEvent(EventCreateRequestDto request) {
        Long productId = request.getProductId();
        int saleRate = request.getSaleRate();

        Product product = productRepository.findById(productId).orElseThrow(
            () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );

        Event event = new Event(product,saleRate);
        eventRepository.save(event);
    }
}
