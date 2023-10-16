package com.example.flashfrenzy.domain.user.service;

import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.order.dto.OrderResponseDto;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.order.repository.OrderRepository;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.user.dto.SignupRequestDto;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import com.example.flashfrenzy.global.data.dto.ItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    OrderRepository orderRepository;

    @Test
    @DisplayName("이미 저장된 유저 이름으로 회원 가입하면 에러 처리한다.")
    void signupCheckUsername() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("test");
        signupRequestDto.setPassword("1234");
        signupRequestDto.setEmail("123@naver.com");
        User user = new User("test", "1234", UserRoleEnum.USER, "123@naver.com", new Basket());
        given(userRepository.findByUsername("test")).willReturn(Optional.of(user));

        UserService userService = new UserService(userRepository, passwordEncoder, orderRepository);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(signupRequestDto));

        // then
        assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이미 저장된 이메일로 회원 가입하면 에러 처리한다.")
    void signupCheckEmail() {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUsername("test");
        signupRequestDto.setPassword("1234");
        signupRequestDto.setEmail("123@naver.com");
        User user = new User("test", "1234", UserRoleEnum.USER, "123@naver.com", new Basket());
        given(userRepository.findByEmail("123@naver.com")).willReturn(Optional.of(user));

        UserService userService = new UserService(userRepository, passwordEncoder, orderRepository);
        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(signupRequestDto));

        // then
        assertEquals("중복된 Email 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문내역 확인")
    void getOrders() {
        // given
        User user = new User();
        Order order1 = new Order();
        Order order2 = new Order();
        order1.addUser(user);
        order2.addUser(user);
        Basket basket = new Basket();
        ItemDto itemDto = new ItemDto(
                "Apple 맥북 프로 16형 2021년 M1 Max 10코어 실버 (MK1H3KH/A)",
                "https://shopping-phinf.pstatic.net/main_2941337/29413376619.20220705152340.jpg",
                3515000L,
                "전자기기",
                "노트북",
                100L
        );
        Product product = new Product(itemDto);
        OrderProduct orderProduct = new OrderProduct(new BasketProduct(2L, basket, product));
        OrderProduct orderProduct2 = new OrderProduct(new BasketProduct(4L, basket, product));
        order1.addOrderProduct(orderProduct);
        order2.addOrderProduct(orderProduct2);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        given(orderRepository.findAllByUser(user)).willReturn(orders);

        UserService userService = new UserService(userRepository, passwordEncoder, orderRepository);
        // when
        List<OrderResponseDto> listOrder = userService.getOrders(user);

        // then
        assertEquals(listOrder.size(), 2);
        assertEquals(listOrder.get(0).getOrderProductList().get(0).getTitle(), "Apple 맥북 프로 16형 2021년 M1 Max 10코어 실버 (MK1H3KH/A)");
        assertEquals(listOrder.get(0).getOrderProductList().get(0).getCount(), 2);
        assertEquals(listOrder.get(1).getOrderProductList().get(0).getTitle(), "Apple 맥북 프로 16형 2021년 M1 Max 10코어 실버 (MK1H3KH/A)");
        assertEquals(listOrder.get(1).getOrderProductList().get(0).getCount(), 4);
    }
}
