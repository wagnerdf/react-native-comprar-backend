package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.wagnerdf.comprar.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {

    private String id;

    private String orderNumber;

    private OrderStatus status;

    private BigDecimal total;
    
    private List<OrderItemResponse> items;

    private DeliveryAddressResponse deliveryAddress;

}