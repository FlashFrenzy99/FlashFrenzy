package com.example.flashfrenzy.domain.user.service;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.order.dto.OrderResponseDto;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.user.dto.SignupRequestDto;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "User API")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        log.debug("회원가입");
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 등록
        Basket basket = new Basket();
        User user = new User(username, password, UserRoleEnum.USER, email, basket);
        userRepository.save(user);

    }

    public List<OrderResponseDto> getOrders(User user, Pageable pageable) {
        log.debug("주문내역 확인");
        Page<Order> orders = orderRepository.findAllByUser(user, pageable);

        return orders.stream().map(OrderResponseDto::new).toList();
    }
}
