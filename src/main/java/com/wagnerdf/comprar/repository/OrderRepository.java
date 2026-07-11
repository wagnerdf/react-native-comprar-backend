package com.wagnerdf.comprar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
	
	Page<Order> findAll(Pageable pageable);
	
	Page<Order> findAllByUserId(
	        String userId,
	        Pageable pageable
	);

}
