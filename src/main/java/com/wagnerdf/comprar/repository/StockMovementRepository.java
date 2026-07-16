package com.wagnerdf.comprar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.StockMovement;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, String> {

}