package com.wagnerdf.comprar.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.service.PermissionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionSynchronizer implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final PermissionService permissionService;

    @Override
    public void run(String... args) {

        log.info("Sincronizando permissões dos usuários...");

        for (Auth auth : authRepository.findAll()) {

            Set<Permission> rolePermissions =
                    permissionService.getPermissionsByRole(auth.getRole());

            Set<Permission> currentPermissions =
                    auth.getPermissions();

            boolean updated = false;

            for (Permission permission : rolePermissions) {

                boolean exists = currentPermissions.stream()
                        .anyMatch(p ->
                                p.getName().equals(permission.getName()));

                if (!exists) {
                    currentPermissions.add(permission);
                    updated = true;
                }
            }

            if (updated) {

                auth.setPermissions(currentPermissions);

                authRepository.save(auth);

                log.info(
                        "Permissões atualizadas para o usuário {}",
                        auth.getUsername()
                );
            }
        }

        log.info("Sincronização das permissões concluída.");
    }
}