package com.wagnerdf.comprar.controller;


import com.wagnerdf.comprar.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.dto.response.AuthResponse;
import com.wagnerdf.comprar.dto.request.LoginRequest;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
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

		String token = userService.login(
	            request.getUsername(),
	            request.getPassword()
	    );

		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	@GetMapping("/me")
	public Map<String, Object> me() {

	    var auth = SecurityContextHolder.getContext().getAuthentication();

	    String username = auth.getName();

	    String role = auth.getAuthorities()
	            .stream()
	            .findFirst()
	            .map(a -> a.getAuthority())
	            .orElse("UNKNOWN");

	    return Map.of(
	            "username", username,
	            "role", role.replace("ROLE_", "")
	    );
	}
	
	@GetMapping("/admin")
	public String admin() {
	    return "Apenas ADMIN!";
	}
}