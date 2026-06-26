package com.wagnerdf.comprar.config;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Gender;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.PermissionRepository;
import com.wagnerdf.comprar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (authRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        User user = User.builder()
                .name("Administrador")
                .email("admin@comprar.com")
                .birthDate(java.time.LocalDate.of(2000, 1, 1))
                .gender(Gender.OTHER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        Set<Permission> permissions = Role.ADMIN.getPermissions()
                .stream()
                .map(permissionEnum ->
                        permissionRepository.findByName(permissionEnum.name())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Permissão não encontrada: "
                                                        + permissionEnum.name())))
                .collect(Collectors.toSet());

        Auth auth = Auth.builder()
                .user(savedUser)
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .role(Role.ADMIN)
                .permissions(permissions)
                .build();

        authRepository.save(auth);

        System.out.println("======================================");
        System.out.println("ADMIN PADRÃO CRIADO");
        System.out.println("Usuário: admin");
        System.out.println("Senha: 123456");
        System.out.println("======================================");
    }
}
