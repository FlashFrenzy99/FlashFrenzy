package com.example.flashfrenzy.domain.user.controller;

import com.example.flashfrenzy.domain.order.dto.OrderResponseDto;
import com.example.flashfrenzy.domain.user.dto.SignupRequestDto;
import com.example.flashfrenzy.domain.user.service.UserService;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/sign-in-page")
    public String signInPage() {
        return "login";
    }

    @GetMapping("/sign-up-page")
    public String signUpPage() {
        return "signup";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return "redirect:/auth/users/sign-in-page";
    }

    @GetMapping("/my-page")
    public String myPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        long startTime = System.currentTimeMillis();
        List<OrderResponseDto> orders = userService.getOrders(userDetails.getUser(), pageable);
        model.addAttribute("orders", orders);
        log.debug("마이페이지 elapsed time : " + (System.currentTimeMillis() - startTime) + "ms.");
        return "my-page";
    }
}