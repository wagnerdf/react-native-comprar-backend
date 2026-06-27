package com.wagnerdf.comprar.config;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Gender;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.PermissionRepository;
import com.wagnerdf.comprar.repository.UserRepository;
import com.wagnerdf.comprar.service.PermissionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;
    
    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        if (authRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        User user = User.builder()
                .name(adminName)
                .email(adminEmail)
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .gender(Gender.OTHER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        Set<Permission> permissions =
                permissionService.getPermissionsByRole(Role.ADMIN);

        Auth auth = Auth.builder()
                .user(savedUser)
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .permissions(permissions)
                .build();

        authRepository.save(auth);

        System.out.println("======================================");
        System.out.println("ADMIN PADRÃO CRIADO");
        System.out.println("Usuário: " + adminUsername);
        System.out.println("Senha: " + adminPassword);
        System.out.println("======================================");
    }
}
