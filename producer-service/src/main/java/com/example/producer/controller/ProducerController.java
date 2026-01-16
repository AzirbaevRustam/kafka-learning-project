package com.example.producer.controller;

import com.example.common.dto.OrderEvent;
import com.example.common.dto.UserEvent;
import com.example.producer.dto.CreateOrderRequest;
import com.example.producer.dto.CreateUserRequest;
import com.example.producer.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProducerController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest request) {
        log.info("Received request to create user: {}", request);

        UserEvent userEvent = UserEvent.create(
                request.getUsername(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
        );

        kafkaProducerService.sendUserEvent(userEvent);

        return ResponseEntity.ok("User creation event sent to Kafka");
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        log.info("Received request to create order: {}", request);

        OrderEvent orderEvent = OrderEvent.create(
                request.getOrderNumber(),
                request.getUserId(),
                request.getItems()
        );

        kafkaProducerService.sendOrderEvent(orderEvent);

        return ResponseEntity.ok("Order creation event sent to Kafka");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Producer service is running");
    }
}