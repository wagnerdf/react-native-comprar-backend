package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.mapper.UserMapper;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(RegisterRequest request) {

        // =========================
        // VALIDAÇÕES
        // =========================
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        	throw new BusinessException("Email já cadastrado");
        }

        if (authRepository.findByUsername(request.getUsername()).isPresent()) {
        	throw new BusinessException("Username já cadastrado");
        }

        // =========================
        // MAPPER
        // =========================
        User user = UserMapper.toEntity(request);

        // =========================
        // REGRAS DE NEGÓCIO
        // =========================
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // =========================
        // AUTH
        // =========================
        Auth auth = Auth.builder()
                .user(savedUser)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        authRepository.save(auth);
    }

    public void login(String username, String password) {

        var auth = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuário ou senha inválidos"));
        
        boolean isValid = passwordEncoder.matches(password, auth.getPassword());

        if (!isValid) {
            throw new AuthenticationException("Usuário ou senha inválidos");
        }
    }
}