package com.wagnerdf.comprar.controller;


import com.wagnerdf.comprar.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.dto.response.AuthResponse;
import com.wagnerdf.comprar.dto.response.UserListResponse;
import com.wagnerdf.comprar.dto.response.UserResponse;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.repository.RefreshTokenRepository;
import com.wagnerdf.comprar.security.JwtService;
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
	
	@GetMapping
	@PreAuthorize("hasAuthority('READ_USER')")
	public ResponseEntity<List<UserListResponse>> getAllUsers() {

	    return ResponseEntity.ok(userService.getAllUsers());
	}
}