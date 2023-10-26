package com.example.flashfrenzy.domain.order.service;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basket.service.BasketService;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.global.kafka.producer.OrderProducer;
import com.example.flashfrenzy.global.redis.RedisRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.util.set.Sets;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Order API")
public class OrderService {

    private final BasketRepository basketRepository;
    private final BasketProductRepository basketProductRepository;
    private final EventRepository eventRepository;
    private final OrderRepository orderRepository;
    private final RedisRepository redisRepository;
    private final BasketService basketService;
    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper;

    public void orderBasketProducts(Long id) {
        log.debug("장바구니 상품 주문");

        // 장바구니 존재 여부 확인
        Basket basket = basketRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 장바구니가 존재하지 않습니다.")
        );

        List<BasketProduct> basketProductList = basketProductRepository.findByBasketId(id);

        if (basketProductList.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 물품이 1개 이상 존재해야 주문이 가능합니다.");
        }

        Set<Long> eventIdList;
        String eventIdListString = redisRepository.getValue("product:sale:list");
        if (eventIdListString != null) {
            try {
                eventIdList = Sets.newHashSet(objectMapper.readValue(eventIdListString, Long[].class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            eventIdList = eventRepository.findProductIdSet();
        }

        List<OrderProduct> orderProductList = basketProductList.stream().map(basketProduct -> {
            if (eventIdList.contains(basketProduct.getProduct().getId())) {

                Event event = eventRepository.findByProductId(basketProduct.getProduct().getId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 이벤트 상품이 없습니다.")
                        );
                return new OrderProduct(basketProduct, event.getSaleRate());
            } else {
                return new OrderProduct(basketProduct);
            }
        }).toList();

        User user = basket.getUser();
        Order order = new Order();
        order.addUser(user);
        for(OrderProduct orderProduct : orderProductList){
            order.addOrderProduct(orderProduct);
        }
        orderRepository.save(order);

        // producer
        orderProducer.createOrder(orderProductList);

        //주문 후 장바구니 내역 삭제
        basketService.clearBasket(basket.getId());

    }
}

