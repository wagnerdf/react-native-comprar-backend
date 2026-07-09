package com.wagnerdf.comprar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, String>{

}
