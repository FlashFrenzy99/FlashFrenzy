package com.example.flashfrenzy.domain.basket.controller;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.service.BasketService;
import com.example.flashfrenzy.domain.basketProdcut.dto.BasketProductResponseDto;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/baskets")
public class BasketController {

    private final BasketService basketService;

    @GetMapping
    public String getBasket(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model){
        List<BasketProductResponseDto> basketProducts = basketService.getBasket(userDetails.getUser());
        model.addAttribute("basketProducts", basketProducts);
        return "basket";
    }

    @PostMapping("/{id}")
    @ResponseBody
    public void addBasket(@PathVariable(value = "id") Long basketId, @ModelAttribute BasketRequestForm requestForm){
        System.out.println("POST HERE");
        basketService.addBasket(basketId, requestForm);
    }

    @DeleteMapping("/{id}")         // basketProduct_id
    public String deleteBasket(@PathVariable(value = "id") Long basketProductId){
        System.out.println("hello");
        basketService.deleteBasket(basketProductId);
        return "redirect:/api/baskets";
    }
}
