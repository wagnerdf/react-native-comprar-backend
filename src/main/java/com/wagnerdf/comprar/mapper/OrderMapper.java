package com.wagnerdf.comprar.mapper;

import java.util.stream.Collectors;

import com.wagnerdf.comprar.dto.response.DeliveryAddressResponse;
import com.wagnerdf.comprar.dto.response.OrderDetailResponse;
import com.wagnerdf.comprar.dto.response.OrderItemResponse;
import com.wagnerdf.comprar.dto.response.OrderListResponse;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.entity.DeliveryAddress;
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
                .items(
                	    order.getItems()
                	            .stream()
                	            .map(OrderMapper::toItemResponse)
                	            .collect(Collectors.toList())
                	)
                .deliveryAddress(
                        toDeliveryAddressResponse(
                                order.getDeliveryAddress()
                        )
                )
                .build();
    }

    public static OrderListResponse toListResponse(Order order) {

        return OrderListResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
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
    
    private static DeliveryAddressResponse toDeliveryAddressResponse(
            DeliveryAddress address) {

        if (address == null) {
            return null;
        }

        return DeliveryAddressResponse.builder()
                .recipientName(address.getRecipientName())
                .zipCode(address.getZipCode())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .reference(address.getReference())
                .build();

    }

}