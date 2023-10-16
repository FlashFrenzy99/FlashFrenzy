package com.example.flashfrenzy.product.controller;

import com.example.flashfrenzy.domain.product.controller.ProductController;
import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @DisplayName("상품 목록 조회 성공")
    @Test
    void getProductSuccess() throws Exception {
        // given
        String query = "맥";
        List<ProductResponseDto> response = new ArrayList<>();

        doReturn(response).when(productService)
                .getProducts();
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)

        );
        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("productList", response))
                .andDo(print())
                .andReturn();

    }

    @DisplayName("상품 검색 성공")
    @Test
    void searchProductsTest() throws Exception {
        // given
        String query = "맥";
        List<ProductResponseDto> response = new ArrayList<>();

        doReturn(response).when(productService)
                .searchProducts(query);
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/products/search").param("query", query)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("productList", response))
                .andDo(print())
                .andReturn();
    }

    @DisplayName("상품 단품 조회 성공")
    @Test
    void detailProductTest() throws Exception {
        // given
        Long productId = 1L;  //Product의 ID 예시
        ProductResponseDto response = new ProductResponseDto();
        doReturn(response).when(productService)
                .detailsProduct(productId);
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/products/" + Long.toString(productId))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andExpect(model().attribute("product", response))
                .andDo(print())
                .andReturn();
    }
}
