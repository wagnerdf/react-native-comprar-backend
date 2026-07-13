package com.wagnerdf.comprar.repository;

import java.util.Optional;

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
	
	Optional<Order> findById(String id);

}
