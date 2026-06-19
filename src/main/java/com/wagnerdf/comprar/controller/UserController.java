package com.wagnerdf.comprar.controller;


import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wagnerdf.comprar.dto.request.RegisterRequest;
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
	public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {

	    userService.createUser(request);

	    return ResponseEntity.status(201).build();
	}
    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        boolean isValid = userService.login(
                request.getUsername(),
                request.getPassword()
        );

        if (isValid) {
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }
}