package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

}