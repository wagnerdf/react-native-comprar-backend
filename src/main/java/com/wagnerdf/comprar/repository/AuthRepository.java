package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.enums.Role;

public interface AuthRepository extends JpaRepository<Auth, String>{
	
	Optional<Auth> findByUsername(String username);
	
	boolean existsByRole(Role role);

}
