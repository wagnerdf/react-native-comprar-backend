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
import com.wagnerdf.comprar.repository.PermissionRepository;
import com.wagnerdf.comprar.repository.RefreshTokenRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import com.wagnerdf.comprar.security.JwtService;
import com.wagnerdf.comprar.entity.Permission;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PermissionRepository permissionRepository;
    private final AuditService auditService;
    private final JwtService jwtService;

    @Transactional
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
        // PERMISSÕES PADRÃO
        // =========================
        Permission readProfile = permissionRepository.findByName("READ_PROFILE")
                .orElseThrow(() -> new RuntimeException("Permissão READ_PROFILE não encontrada"));

        // =========================
        // ROLE PADRÃO
        // =========================
        Role role = Role.USER;

        // =========================
        // AUTH
        // =========================
        Auth auth = Auth.builder()
                .user(savedUser)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .permissions(Set.of(readProfile))
                .build();

        authRepository.save(auth);
    }

    @Transactional
    public AuthResponse login(String username, String password) {

        var auth = authRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Usuário ou senha inválidos"));
        
        if(!passwordEncoder.matches(password, auth.getPassword())) {
        	throw new AuthenticationException("Usuário ou senha inválidos");
        }
        
        String accessToken = jwtService.generateToken(username, auth.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(username) ;
        
        refreshTokenRepository.deleteAllByUsername(username);
        
        refreshTokenRepository.save(
        		RefreshToken.builder()
        			.token(refreshToken)
        			.username(username)
        			.expiration(LocalDateTime.now().plusDays(7))
        			.build()
        );
        
        auditService.log(username, "LOGIN");
        return new AuthResponse(accessToken, refreshToken);
        
    }
    
    public void logout(String refreshToken) {

        var token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new AuthenticationException("Refresh token inválido"));

        refreshTokenRepository.delete(token);
        auditService.log(token.getUsername(), "LOGOUT");
    }
}













