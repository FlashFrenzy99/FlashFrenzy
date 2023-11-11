package com.example.flashfrenzy.global.kafka.producer;


import com.example.flashfrenzy.domain.orderProduct.entity.OrderProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void createOrder(List<OrderProduct> orderProductList) {

        try {
            String orderMessage = objectMapper.writeValueAsString(orderProductList); // <- 흠
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("order-2",
                    orderMessage);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug(
                            "Send message=[ orderId: " + orderProductList.get(0).getOrder().getId()
                                    + " ] with partition=[" + result.getRecordMetadata().partition()
                                    + "], offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    log.error("Unable to send message=[ orderId: " + orderProductList.get(0)
                            .getOrder().getId() + " ] due to : " + ex.getMessage());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("메세지 변환 실패");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
