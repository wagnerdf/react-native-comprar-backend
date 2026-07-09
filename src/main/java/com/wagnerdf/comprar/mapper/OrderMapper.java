package com.wagnerdf.comprar.mapper;

import java.util.stream.Collectors;

import com.wagnerdf.comprar.dto.response.OrderDetailResponse;
import com.wagnerdf.comprar.dto.response.OrderItemResponse;
import com.wagnerdf.comprar.dto.response.OrderListResponse;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.entity.Order;
import com.wagnerdf.comprar.entity.OrderItem;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .total(order.getTotal())
                .build();

    }

    public static OrderListResponse toListResponse(Order order) {

        return OrderListResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .total(order.getTotal())
                .build();

    }

    public static OrderDetailResponse toDetailResponse(Order order) {

        return OrderDetailResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getUser().getName())
                .status(order.getStatus())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .items(order.getItems()
                        .stream()
                        .map(OrderMapper::toItemResponse)
                        .collect(Collectors.toList()))
                .build();

    }

    private static OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();

    }

}