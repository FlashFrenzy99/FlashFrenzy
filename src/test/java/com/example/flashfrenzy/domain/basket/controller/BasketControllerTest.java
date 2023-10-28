package com.example.flashfrenzy.domain.basket.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.flashfrenzy.domain.basket.dto.BasketRequestForm;
import com.example.flashfrenzy.domain.basket.entity.Basket;
import com.example.flashfrenzy.domain.basket.service.BasketService;
import com.example.flashfrenzy.domain.basketProdcut.dto.BasketProductResponseDto;
import com.example.flashfrenzy.domain.user.entity.User;
import com.example.flashfrenzy.domain.user.entity.UserRoleEnum;
import com.example.flashfrenzy.global.config.WebSecurityConfig;
import com.example.flashfrenzy.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(
    controllers = {BasketController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
@MockBean(JpaMetamodelMappingContext.class)
class BasketControllerTest {

    @Autowired
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BasketService basketService;

    @BeforeEach //로그인 처리
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "test";
        String password = "test123";
        UserRoleEnum role = UserRoleEnum.USER;
        String email = "test@sparta.com";
        User testUser = new User(username, password, role, email, new Basket());
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("장바구니 상품 조회 테스트")
    void getBasket_test() throws Exception{
        //given
        this.mockUserSetup();
        List<BasketProductResponseDto> responseDtoList = new ArrayList<>();

        mvc.perform(get("/api/baskets")
                .principal(mockPrincipal)
            )
            .andExpect(status().isOk())
            .andExpect(view().name("basket"))
            .andExpect(model().attributeExists("basketProducts"))
            .andExpect(model().attribute("basketProducts", responseDtoList));

    }

    @Test
    @DisplayName("장바구니에 특정 상품 추가")
    void addBasket_test() throws Exception{
        //given
        BasketRequestForm requestForm = new BasketRequestForm(1L, 10L);

        //then
        mvc.perform(post("/api/baskets/{id}",1L)
                .flashAttr("requestForm", requestForm))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/api/baskets"));
    }

    @Test
    @DisplayName("")
    void deleteBasket_test() throws Exception{
        //given

        //then
        mvc.perform(delete("/api/baskets/{id}",1L))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/api/baskets"));
    }
}