package com.example.flashfrenzy.domain.product.controller;

import com.example.flashfrenzy.domain.product.dto.ProductResponseDto;
import com.example.flashfrenzy.domain.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
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
        doReturn(response).when(productService.getProducts());
        Pageable pageable = Pageable.ofSize(20);
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

        Pageable pageable = Pageable.ofSize(20);
        Page<ProductResponseDto> responsePage = new PageImpl<>(response, pageable, 1);

        MultiValueMap<String, String> pageable2 = new LinkedMultiValueMap<>();
        pageable2.add("page", "1");
        pageable2.add("size", "15");

        when(productService.searchProducts(query,pageable)).thenReturn(responsePage);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/products/search")
                        .param("query", query)
                        .params(pageable2)
//                        .param("page", "1")
//                        .param("size", "15")
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

    private final Page<ProductResponseDto> response = new Page<ProductResponseDto>() {
        @Override
        public int getTotalPages() {
            return 0;
        }

        @Override
        public long getTotalElements() {
            return 0;
        }

        @Override
        public <U> Page<U> map(Function<? super ProductResponseDto, ? extends U> converter) {
            return null;
        }

        @Override
        public int getNumber() {
            return 0;
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public int getNumberOfElements() {
            return 0;
        }

        @Override
        public List<ProductResponseDto> getContent() {
            return null;
        }

        @Override
        public boolean hasContent() {
            return false;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public boolean isFirst() {
            return false;
        }

        @Override
        public boolean isLast() {
            return false;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Pageable nextPageable() {
            return null;
        }

        @Override
        public Pageable previousPageable() {
            return null;
        }

        @Override
        public Iterator<ProductResponseDto> iterator() {
            return null;
        }
    };

}
