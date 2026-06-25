package com.wagnerdf.comprar.controller;


import com.wagnerdf.comprar.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.dto.request.UpdateUserRequest;
import com.wagnerdf.comprar.dto.response.AuthResponse;
import com.wagnerdf.comprar.dto.response.UserDetailResponse;
import com.wagnerdf.comprar.dto.response.UserListResponse;
import com.wagnerdf.comprar.dto.response.UserResponse;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.repository.RefreshTokenRepository;
import com.wagnerdf.comprar.security.JwtService;
import com.wagnerdf.comprar.annotation.Auditable;
import com.wagnerdf.comprar.dto.request.LoginRequest;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtService jwtService;
	
	// ================
	// REGISTER
	// ================
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

	    userService.createUser(request);

	    return ResponseEntity.status(201).build();
	}
    // =========================
    // LOGIN
    // =========================
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

		AuthResponse response = userService.login(
	            request.getUsername(),
	            request.getPassword()
	    );

		return ResponseEntity.ok(response);
	}
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public UserResponse me() {

	    var auth = SecurityContextHolder.getContext().getAuthentication();

	    String username = auth.getName();

	    String role = auth.getAuthorities()
	            .stream()
	            .map(a -> a.getAuthority())
	            .filter(a -> a.startsWith("ROLE_"))
	            .findFirst()
	            .orElse("ROLE_UNKNOWN")
	            .replace("ROLE_", "");

	    return new UserResponse(
	            username,
	            role.replace("ROLE_", "")
	    );
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
	    return "Apenas ADMIN!";
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {

	    String refreshToken = body.get("refreshToken");

	    var tokenEntity = refreshTokenRepository.findByToken(refreshToken)
	            .orElseThrow(() -> new AuthenticationException("Refresh token inválido"));

	    if (tokenEntity.getExpiration().isBefore(LocalDateTime.now())) {
	        throw new AuthenticationException("Refresh token expirado");
	    }

	    String newAccessToken = jwtService.generateToken(
	            tokenEntity.getUsername(),
	            "USER"
	    );

	    return ResponseEntity.ok(
	            new AuthResponse(newAccessToken, refreshToken)
	    );
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody Map<String, String> body) {

	    String refreshToken = body.get("refreshToken");

	    userService.logout(refreshToken);

	    return ResponseEntity.ok().build();
	}
	
	/**
	 * 🔍 Lista usuários com paginação e filtros opcionais
	 *
	 * 📌 Endpoint:
	 * GET /users
	 *
	 * 📌 Exemplos de uso:
	 *
	 * ✔ Listar usuários (padrão)
	 * GET /users
	 *
	 * ✔ Paginação
	 * GET /users?page=0&size=5
	 *
	 * ✔ Buscar por nome
	 * GET /users?name=wagner
	 *
	 * ✔ Buscar por email
	 * GET /users?email=@gmail
	 *
	 * ✔ Combinar filtros + paginação
	 * GET /users?page=0&size=5&name=wag&email=@email
	 *
	 * 🔐 Segurança:
	 * - Requer permissão: READ_USER
	 *
	 * 📌 Retorno:
	 * - Lista paginada de usuários (Page<UserListResponse>)
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('READ_USER')")
	public ResponseEntity<Page<UserListResponse>> getAllUsers(
			
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String name,
	        @RequestParam(required = false) String email
			
		) {

	    return ResponseEntity.ok(userService.getAllUsers(page, size, name, email));
	}
	
	/**
	 * 🔍 Busca usuário por ID
	 *
	 * 📌 Endpoint:
	 * GET /users/{id}
	 *
	 * 📌 Exemplo:
	 * GET /users/123
	 *
	 * 🔐 Segurança:
	 * - Requer permissão: READ_USER
	 *
	 * 📌 Retorno:
	 * - Dados detalhados do usuário
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_USER')")
	public ResponseEntity<UserDetailResponse> getUserById(
	        @PathVariable String id
	) {
	    return ResponseEntity.ok(userService.getUserById(id));
	}
	
	/**
	 * ✏️ Atualiza dados do usuário
	 *
	 * Regras:
	 * - ADMIN pode alterar qualquer usuário
	 * - USER pode alterar apenas o próprio cadastro
	 *
	 * Campos permitidos:
	 * - name
	 * - birthDate
	 * - gender
	 *
	 * Campos bloqueados:
	 * - email
	 *
	 * Exemplo:
	 * PUT /users/{id}
	 *
	 * Permissão:
	 * UPDATE_USER
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('UPDATE_USER')")
	@Auditable(action = "UPDATE_USER")
	public ResponseEntity<Void> updateUser(
	        @PathVariable String id,
	        @Valid @RequestBody UpdateUserRequest request
	) {

	    userService.updateUser(id, request);

	    return ResponseEntity.ok().build();
	}
	
	/**
	 * 🗑️ Desativa um usuário (Soft Delete)
	 *
	 * Regras:
	 * - ADMIN pode desativar qualquer usuário
	 * - USER pode desativar apenas a própria conta
	 *
	 * O usuário não é removido do banco.
	 * Apenas recebe:
	 *
	 * active = false
	 *
	 * Permissão:
	 * DELETE_USER
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_USER')")
	@Auditable(action = "DELETE_USER")
	public ResponseEntity<Void> deleteUser(
	        @PathVariable String id
	) {

	    userService.deleteUser(id);

	    return ResponseEntity.noContent().build();
	}
}










