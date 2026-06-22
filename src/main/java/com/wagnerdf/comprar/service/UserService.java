package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.dto.response.AuthResponse;
import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.RefreshToken;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.mapper.UserMapper;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.RefreshTokenRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import com.wagnerdf.comprar.security.JwtService;

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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

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
                .role(Role.USER)
                .build();

        authRepository.save(auth);
    }

    public AuthResponse login(String username, String password) {

        var auth = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuário ou senha inválidos"));
        
        if(!passwordEncoder.matches(password, auth.getPassword())) {
        	throw new AuthenticationException("Usuário ou senha inválidos");
        }
        
        String accessToken = jwtService.generateToken(username, auth.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(username) ;
        
        refreshTokenRepository.deleteByUsername(username);
        
        refreshTokenRepository.save(
        		RefreshToken.builder()
        			.token(refreshToken)
        			.username(username)
        			.expiration(LocalDateTime.now().plusDays(7))
        			.build()
        );
        
        return new AuthResponse(accessToken, refreshToken);
        
    }
    
    public void logout(String refreshToken) {

        var token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Refresh token inválido"));

        refreshTokenRepository.delete(token);
    }
}













