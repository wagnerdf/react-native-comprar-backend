package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.dto.response.AuthResponse;
import com.wagnerdf.comprar.dto.response.UserDetailResponse;
import com.wagnerdf.comprar.dto.response.UserListResponse;
import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.RefreshToken;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.UserNotFoundException;
import com.wagnerdf.comprar.mapper.UserMapper;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.RefreshTokenRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import com.wagnerdf.comprar.security.JwtService;
import com.wagnerdf.comprar.specification.UserSpecification;
import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.dto.request.UpdateUserRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PermissionService permissionService;
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
        
        // =========================
        // ACTIVE USER
        // =========================
        user.setActive(true);

        User savedUser = userRepository.save(user);

        // =========================
        // PERMISSÕES PADRÃO
        // =========================
        Set<Permission> permissions =
                permissionService.getPermissionsByRole(Role.USER);

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
                .permissions(permissions)
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
    
    public Page<UserListResponse> getAllUsers(int page, int size, String name, String email) {
    	
    	Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
    	
    	Specification<User> spec = Specification
    			.where(UserSpecification.isActive())
                .and(UserSpecification.nameContains(name))
                .and(UserSpecification.emailContains(email));

        return userRepository.findAll(spec, pageable)
                .map(user -> new UserListResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getGender()
                ));
    }
    
    public UserDetailResponse getUserById(String id) {

        User user = userRepository.findByIdAndActiveTrue(id)
        		.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGender()
        );
    }
    
    public void updateUser(String id, UpdateUserRequest request) {

        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {

            Auth auth = authRepository.findByUsername(loggedUsername)
                    .orElseThrow(() ->
                            new AuthenticationException("Usuário não autenticado"));

            if (!auth.getUser().getId().equals(id)) {
                throw new AuthenticationException(
                        "Você não pode alterar outro usuário"
                );
            }
        }

        user.setName(request.getName());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
    
    public void deleteUser(String id) {

    	User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {

            Auth auth = authRepository.findByUsername(loggedUsername)
                    .orElseThrow(() ->
                            new AuthenticationException("Usuário não autenticado"));

            if (!auth.getUser().getId().equals(id)) {
                throw new AuthenticationException(
                        "Você não pode excluir outro usuário"
                );
            }
        }

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
    
    
}













