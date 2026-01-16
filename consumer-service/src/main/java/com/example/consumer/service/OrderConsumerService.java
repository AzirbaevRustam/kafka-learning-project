package com.example.consumer.service;

import com.example.common.dto.OrderEvent;
import com.example.common.dto.OrderItem;
import com.example.consumer.entity.OrderEntity;
import com.example.consumer.entity.OrderItemEntity;
import com.example.consumer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumerService {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "${spring.kafka.topics.order-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    @Transactional
    public void consumeOrderEvent(@Payload OrderEvent orderEvent) {
        log.info("Received OrderEvent: {}", orderEvent);

        try {
            boolean orderExists = orderRepository.existsByOrderNumber(orderEvent.getOrderNumber());

            if (orderExists) {
                log.warn("Order with number {} already exists. Skipping.", orderEvent.getOrderNumber());
                return;
            }

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderNumber(orderEvent.getOrderNumber());
            orderEntity.setUserId(orderEvent.getUserId());
            orderEntity.setTotalAmount(orderEvent.getTotalAmount());
            orderEntity.setStatus(orderEvent.getStatus());
            orderEntity.setOrderDate(orderEvent.getOrderDate());

            orderEntity.setItems(
                    orderEvent.getItems().stream()
                            .map(item -> convertToOrderItemEntity(item, orderEntity))
                            .collect(Collectors.toList())
            );

            OrderEntity savedOrder = orderRepository.save(orderEntity);

            log.info("Order saved successfully with ID: {}. Total items: {}",
                    savedOrder.getId(), savedOrder.getItems().size());

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation for order with number {}: {}",
                    orderEvent.getOrderNumber(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing OrderEvent: {}", e.getMessage(), e);
        }
    }

    private OrderItemEntity convertToOrderItemEntity(OrderItem orderItem, OrderEntity orderEntity) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        itemEntity.setOrder(orderEntity);
        itemEntity.setProductId(orderItem.getProductId());
        itemEntity.setProductName(orderItem.getProductName());
        itemEntity.setQuantity(orderItem.getQuantity());
        itemEntity.setPrice(orderItem.getPrice());
        return itemEntity;
    }
}