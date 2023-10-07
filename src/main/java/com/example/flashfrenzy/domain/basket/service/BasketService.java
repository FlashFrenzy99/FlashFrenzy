package com.example.flashfrenzy.domain.basket.service;

import com.example.flashfrenzy.domain.basket.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

}
