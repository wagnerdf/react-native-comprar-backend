package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.mapper.UserMapper;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    public void createUser(RegisterRequest request) {

        // =========================
        // VALIDAÇÕES
        // =========================
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (authRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username já cadastrado");
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
                .password(request.getPassword())
                .build();

        authRepository.save(auth);
    }

    public boolean login(String username, String password) {

        return authRepository.findByUsername(username)
                .map(auth -> auth.getPassword().equals(password))
                .orElse(false);
    }
}