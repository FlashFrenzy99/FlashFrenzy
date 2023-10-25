package com.example.flashfrenzy.domain.order.service;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.orderProduct.entity.StatusEnum;
import com.example.flashfrenzy.domain.stock.service.StockService;
import com.example.flashfrenzy.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
@Slf4j(topic = "Order API")
public class OrderService {

    private final BasketRepository basketRepository;
    private final BasketProductRepository basketProductRepository;
    private final EventRepository eventRepository;
    private final RedissonClient redissonClient;
    private final StockService stockService;
    private final OrderRepository orderRepository;

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

        Set<Long> eventIdList = eventRepository.findProductIdSet();

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

        Order order = new Order();
        order.addUser(user);


        // Order에 orderproduct를 넣고 orderproduct의 status를 진행중으로 한 상태로 보낸다
        // 만약 성공하면 재고 차감하고 status 성공, 아니면 롤백하고 실패로 변경
        for (OrderProduct orderProduct : orderProductList) {


            //재고 서비스에서 재고처리 전부 담당하게?
            Long productId = orderProduct.getProduct().getId();

            RLock lock = redissonClient.getLock("product" + productId);
            try {
                //락 획득 시도
                boolean available = lock.tryLock(20, 5, TimeUnit.SECONDS);

                //락 획득 실패
                if (!available) {
                    log.error("주문 시도 중 lock 획득 실패");
                    orderProduct.updateStatus(StatusEnum.FAIL);
                    continue;
                }
                stockService.decrease(productId, orderProduct.getCount());

                orderProduct.updateStatus(StatusEnum.SUCCESS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e ) {
                log.error("재고 부족으로 구매 실패");
                orderProduct.updateStatus(StatusEnum.FAIL);
            }
            finally {
                order.addOrderProduct(orderProduct);
                lock.unlock();
            }

        }
        orderRepository.save(order);
        //주문 후 장바구니 내역 삭제
        basketProductRepository.deleteAllByBasket(basket);
    }

}
