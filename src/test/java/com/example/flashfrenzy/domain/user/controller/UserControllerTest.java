package com.example.flashfrenzy.domain.user.controller;

import com.example.flashfrenzy.domain.basket.controller.MockSpringSecurityFilter;
import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basketProdcut.entity.BasketProduct;
import com.example.flashfrenzy.domain.order.entity.Order;
import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.domain.user.repository.UserRepository;
import com.example.flashfrenzy.domain.user.service.UserService;
import com.example.flashfrenzy.global.config.WebSecurityConfig;
import com.example.flashfrenzy.global.data.dto.ItemDto;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import com.example.flashfrenzy.global.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest( // test쪽 컨트롤러 테스트 설정
    controllers = {UserController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {

    @MockBean
    UserService userService;

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        Basket basket = new Basket();
        User user = new User("test", "1234", UserRoleEnum.USER, "123@naver.com", basket);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(user);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("로그인 Page 접속")
    void signInPage() throws Exception {
        // when - then
        mvc.perform(get("/auth/users/sign-in-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 Page 접속")
    void signUpPage() throws Exception {
        // when - then
        mvc.perform(get("/auth/users/sign-up-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 요청 처리")
    void signUp() throws Exception {
        // given
        MultiValueMap<String, String> signupRequestForm = new LinkedMultiValueMap<>();
        signupRequestForm.add("username", "test");
        signupRequestForm.add("password", "1234");
        signupRequestForm.add("email", "123@naver.com");

        // when - then
        mvc.perform(post("/auth/users/sign-up")
                        .params(signupRequestForm)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/auth/users/login-page"))
                .andDo(print());
    }

    @Test
    @DisplayName("마이페이지 요청 처리")
    void myPage() throws Exception {
        // given
        this.mockUserSetup();

        // when - then
        mvc.perform(get("/auth/users/my-page")
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("my-page"))
                .andExpect(model().attributeExists("orders"))
                .andDo(print());
    }

    @Test
    @Disabled
    @DisplayName("로그인 요청 처리")
    void signIn() throws Exception {
        // given
        this.mockUserSetup();
        MultiValueMap<String, String> signinRequestForm = new LinkedMultiValueMap<>();
        signinRequestForm.add("username", "user");
        signinRequestForm.add("password", "1234");

        // when - then
        mvc.perform(formLogin("/auth/users/sign-in")
                        .user(signinRequestForm.get("username").toString())
                        .password(signinRequestForm.get("password").toString())
//                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/"))
                .andDo(print());
    }
}
