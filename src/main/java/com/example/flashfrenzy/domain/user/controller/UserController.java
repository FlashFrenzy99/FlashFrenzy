package com.example.flashfrenzy.domain.user.controller;

import com.example.flashfrenzy.domain.user.dto.LoginRequestDto;
import com.example.flashfrenzy.domain.user.dto.SignupRequestDto;
import com.example.flashfrenzy.domain.user.service.UserService;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/sign-in-page")
    public String signinPage() {
        return "login";
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/sign-up-page")
    public String signUpPage() {
        return "signup";
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/sign-up")
    public String signUp(SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return "redirect:/auth/users/login-page";
    }

    /**
     * 로그인 API
     */
    @PostMapping("/sign-in")
    public String signIn(LoginRequestDto requestDto) {

        return "redirect:/";
    }
}
