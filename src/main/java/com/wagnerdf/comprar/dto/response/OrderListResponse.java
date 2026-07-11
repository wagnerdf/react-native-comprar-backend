package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.wagnerdf.comprar.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderListResponse {

    private String id;

    private String orderNumber;

    private OrderStatus status;

    private BigDecimal total;
    
    private LocalDateTime createdAt;

}