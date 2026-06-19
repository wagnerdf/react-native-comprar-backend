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
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .build();

        userService.createUser(
                user,
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok("Usuário criado com sucesso!");
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