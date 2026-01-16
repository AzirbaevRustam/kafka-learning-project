package com.example.producer.dto;

import com.example.common.dto.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String orderNumber;
    private Long userId;
    private List<OrderItem> items;
}