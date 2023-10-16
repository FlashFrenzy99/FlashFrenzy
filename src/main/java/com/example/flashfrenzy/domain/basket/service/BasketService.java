package com.example.flashfrenzy.domain.basket.service;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import com.example.flashfrenzy.domain.basketProdcut.dto.BasketProductResponseDto;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.basketProdcut.repository.BasketProductRepository;
import com.example.flashfrenzy.domain.event.entity.Event;
import com.example.flashfrenzy.domain.event.repository.EventRepository;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Basket API")
@Transactional(readOnly = true)
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketProductRepository basketProductRepository;
    private final ProductRepository productRepository;
    private final EventRepository eventRepository;

    /**
     * 최적화 완료
     */
    public List<BasketProductResponseDto> getBasket(User user) {
        log.debug("장바구니 조회");
        Basket basket = basketRepository.findById(user.getBasket().getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 장바구니를 찾을 수 없습니다."));

        List<BasketProduct> list = basketProductRepository.findByBasketId(basket.getId());


        List<Long> eventIdList = eventRepository.findProductIdList();

        return list.stream()
                .map(basketProduct -> {
                    if (eventIdList.contains(basketProduct.getProduct().getId())) {
                        Event event = eventRepository.findById(basketProduct.getProduct().getId()).orElseThrow();
                        return new BasketProductResponseDto(basketProduct,event.getSaleRate());
                    }else {
                        return new BasketProductResponseDto(basketProduct);
                    }
                }).toList();
    }

    @Transactional
    public void addBasket(Long basketId, BasketRequestForm basketRequestDto) {
        log.debug("장바구니 담기");
        Basket basket = basketRepository.findById(basketId).orElseThrow(() ->
                new IllegalArgumentException("해당 장바구니를 찾을 수 없습니다.")
        );
        Product product = productRepository.findById(basketRequestDto.getProductId())
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
                );

        Optional<BasketProduct> optionalBasketProduct = basketProductRepository.findByBasketAndProduct(
                basket, product);

        // 이미 장바구니에 물품이 존재한다면 수량만 더해주기
        if (optionalBasketProduct.isPresent()) {
            log.debug("장바구니에 물품이 존재 할경우 처리");
            BasketProduct duplicatedBasketProduct = optionalBasketProduct.get();
            long count = duplicatedBasketProduct.getCount() + basketRequestDto.getCount();
            duplicatedBasketProduct.countUpdate(count);
            return;
        }

        // 해당 상품과 갯수를 사용해서 basketProduct 생성
        BasketProduct basketProduct = new BasketProduct(basketRequestDto.getCount(), basket, product);
        basket.getList().add(basketProduct);
        basketProductRepository.save(basketProduct);
    }

    @Transactional
    public void deleteBasket(Long basketProductId) {
        log.debug("장바구니 상품 삭제");
        BasketProduct basketProduct = basketProductRepository.findById(basketProductId).orElseThrow(
                () -> new IllegalArgumentException("장바구니 내부에 상품이 존재하지 않습니다.")
        );
        basketProductRepository.delete(basketProduct);
    }
}
