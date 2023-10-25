package com.example.flashfrenzy.domain.event.service;

import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.stock.entity.Stock;
import com.example.flashfrenzy.domain.stock.repository.StockRepository;
import com.example.flashfrenzy.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "Event API")
public class EventService {

    private final EventRepository eventRepository;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;
    private final StockRepository stockRepository;

    public List<ProductResponseDto> getEventProductList() {

        List<ProductResponseDto> productList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String stock = redisRepository.getValue("product:sale:" + i + ":stock");
            String price = redisRepository.getValue("product:sale:" + i + ":price");
            String productString = redisRepository.getValue("product:sale:" + i);
            if (productString != null) {
                //캐시 hit
                try {
                    Product product = objectMapper.readValue(productString, Product.class);
                    productList.add(new ProductResponseDto(product, Long.parseLong(price), Long.parseLong(stock)));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //캐시 miss 시에 실제 db에서 데이터를 불러오기
                List<Event> findProductList = eventRepository.findEventProductList();
                Product product = findProductList.get(i - 1).getProduct();
                productList.add(new ProductResponseDto(product));
                int rate = findProductList.get(i - 1).getSaleRate();

                try {
                    productString = objectMapper.writeValueAsString(product);
                    Stock selectStock = stockRepository.findById(product.getId()).orElseThrow();
                    Long selectStockValue = selectStock.getStock();
                    redisRepository.save("product:sale:" + i + ":stock", String.valueOf(selectStockValue));
                    redisRepository.save("product:sale:"+ i + ":price", String.valueOf(product.getPrice()*(100 - rate)/100));
                    redisRepository.save("product:sale:" + i, productString);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return productList;
    }
}
