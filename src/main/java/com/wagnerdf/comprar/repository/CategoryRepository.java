package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

}
