package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findBySku(String sku);

    // =========================
    // VALIDAÇÕES
    // =========================
    boolean existsBySku(String sku);
    
    // =========================
    // LISTAGEM
    // =========================
    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );


}