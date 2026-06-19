package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	
	public void createUser(User user, String username, String password ) {
		
		// validar se o email já existe
		userRepository.findByEmail(user.getEmail())
			.ifPresent(u ->{
				throw new RuntimeException("Email já cadastrado");
			});
		
		// validar se o username já existe
		authRepository.findByUsername(username)
			.ifPresent(u ->{
				throw new RuntimeException("Username já cadastrado");
			});
		
		// preencher datas
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        // salvar usuário
        User savedUser = userRepository.save(user);
        
        // criar auth
        Auth auth = Auth.builder()
        		.user(savedUser)
        		.username(username)
        		.password(password)
        		.build();
        
        // salvar auth
        authRepository.save(auth);
		
	}
	
	public boolean login(String username, String password) {

	    return authRepository.findByUsername(username)
	            .map(auth -> auth.getPassword().equals(password))
	            .orElse(false);
	}
}









