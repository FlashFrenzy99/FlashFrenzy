package com.example.flashfrenzy.domain.basket.controller;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.service.BasketService;
import com.example.flashfrenzy.domain.basketProdcut.dto.BasketProductResponseDto;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/baskets")
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    public String getBasket(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model){
        long startTime = System.currentTimeMillis();
        List<BasketProductResponseDto> basketProducts = basketService.getBasket(userDetails.getUser());
        log.info("장바구니 조회 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        model.addAttribute("basketProducts", basketProducts);
        return "basket";
    }

    @PostMapping("/{id}")
    //@ResponseBody
    public String addBasket(@PathVariable(value = "id") Long basketId, @ModelAttribute BasketRequestForm requestForm){
        long startTime = System.currentTimeMillis();
        basketService.addBasket(basketId, requestForm);
        log.info("장바구니 담기 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        return "redirect:/api/baskets";
    }
    @DeleteMapping("/{id}")         // basketProduct_id
    public String deleteBasket(@PathVariable(value = "id") Long basketProductId){
        long startTime = System.currentTimeMillis();
        basketService.deleteBasket(basketProductId);
        log.info("장바구니 상품 삭제 elapsed time : "  + (System.currentTimeMillis() - startTime) + "ms.");
        return "redirect:/api/baskets";
    }
}
