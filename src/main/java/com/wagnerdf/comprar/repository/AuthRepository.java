package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Auth;

public interface AuthRepository extends JpaRepository<Auth, String>{
	
	Optional<Auth> findByUsername(String username);

}
