package com.wagnerdf.comprar.repository;

import com.wagnerdf.comprar.entity.Permission; 
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    Optional<Permission> findByName(String name);
}
