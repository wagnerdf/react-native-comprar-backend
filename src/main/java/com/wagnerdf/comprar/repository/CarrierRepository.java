package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Carrier;

public interface CarrierRepository
        extends JpaRepository<Carrier, String> {

    Optional<Carrier> findByName(String name);
    
    Optional<Carrier> findById(String id);
    
    Optional<Carrier> findByNameIgnoreCase(String name);

}