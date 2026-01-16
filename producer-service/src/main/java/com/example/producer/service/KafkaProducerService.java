package com.example.producer.service;

import com.example.common.config.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.example.common.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserEvent(UserEvent userEvent) {
        String topic = KafkaTopics.USER_TOPIC;
        String key = userEvent.getEmail();

        log.info("Sending user event to topic {}: {}", topic, userEvent);

        kafkaTemplate.send(topic, key, userEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("User event sent successfully. Offset: {}",
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send user event", ex);
                    }
                });
    }

    public void sendOrderEvent(OrderEvent orderEvent) {
        String topic = KafkaTopics.ORDER_TOPIC;
        String key = orderEvent.getOrderNumber();

        log.info("Sending order event to topic {}: {}", topic, orderEvent);

        kafkaTemplate.send(topic, key, orderEvent)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Order event sent successfully. Offset: {}",
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send order event", ex);
                    }
                });
    }
}