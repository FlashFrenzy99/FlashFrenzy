package com.example.flashfrenzy.global.data.service;

import com.example.flashfrenzy.domain.product.entity.Product;
import com.example.flashfrenzy.domain.product.repository.ProductRepository;
import com.example.flashfrenzy.global.data.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j(topic = "NAVER API")
@Service
public class NaverApiService {

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    public NaverApiService(RestTemplateBuilder builder, ProductRepository productRepository) {
        this.restTemplate = builder.build();
        this.productRepository = productRepository;
    }

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    public void searchItems(String query) {
        long start = System.currentTimeMillis();
        // 요청 URL 만들기
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            URI uri = UriComponentsBuilder
                    .fromUriString("https://openapi.naver.com")
                    .path("/v1/search/shop.json")
                    .queryParam("display", 100)
                    .queryParam("start", i)
                    .queryParam("query", query)
                    .queryParam("sort" , "date")
                    .encode()
                    .build()
                    .toUri();
            log.info("uri = " + uri);

            RequestEntity<Void> requestEntity = RequestEntity
                    .get(uri)
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .build();

            ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

            log.info("NAVER API Status Code : " + responseEntity.getStatusCode());

            List<ItemDto> itemDtoList = fromJSONtoItems(responseEntity.getBody());

            itemDtoList.forEach(itemDto -> {
                Product product = new Product(itemDto);
                productList.add(product);
            });
        }
        this.productRepository.saveAll(productList);
        log.info("elapsed time : " + (System.currentTimeMillis() - start) + "ms");
    }

    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items  = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int j = 0; j < 1000; j++) {
            for (Object item : items) {
                ItemDto itemDto = new ItemDto((JSONObject) item);
                itemDtoList.add(itemDto);
            }
        }

        return itemDtoList;
    }
}