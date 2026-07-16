package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.entity.Order;
import com.wagnerdf.comprar.entity.Product;
import com.wagnerdf.comprar.entity.StockMovement;
import com.wagnerdf.comprar.enums.StockMovementType;
import com.wagnerdf.comprar.repository.StockMovementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public void registerExit(
            Product product,
            Integer quantity,
            Integer previousStock,
            Order order) {

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.EXIT)
                .quantity(quantity)
                .previousStock(previousStock)
                .currentStock(product.getStock())
                .order(order)
                .createdAt(LocalDateTime.now())
                .build();

        stockMovementRepository.save(movement);
    }

    public void registerEntry(
            Product product,
            Integer quantity,
            Integer previousStock,
            Order order) {

        StockMovement movement = StockMovement.builder()
                .product(product)
                .type(StockMovementType.ENTRY)
                .quantity(quantity)
                .previousStock(previousStock)
                .currentStock(product.getStock())
                .order(order)
                .createdAt(LocalDateTime.now())
                .build();

        stockMovementRepository.save(movement);
    }

}