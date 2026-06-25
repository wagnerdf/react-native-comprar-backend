package com.wagnerdf.comprar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wagnerdf.comprar.entity.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User>{
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByIdAndActiveTrue(String id);

}
