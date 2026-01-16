package com.example.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long id;
    private String orderNumber;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;

    public static OrderEvent create(String orderNumber, Long userId, List<OrderItem> items) {
        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderEvent(
                null,
                orderNumber,
                userId,
                items,
                total,
                "CREATED",
                LocalDateTime.now()
        );
    }
}