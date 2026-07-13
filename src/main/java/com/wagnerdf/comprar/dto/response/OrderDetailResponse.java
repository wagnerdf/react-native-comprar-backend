package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wagnerdf.comprar.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderDetailResponse {

    private String id;

    private String orderNumber;

    private String customerName;

    private OrderStatus status;

    private BigDecimal total;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;

}