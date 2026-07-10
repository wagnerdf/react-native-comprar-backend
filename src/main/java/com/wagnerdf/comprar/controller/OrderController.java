package com.wagnerdf.comprar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.CreateOrderRequest;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
