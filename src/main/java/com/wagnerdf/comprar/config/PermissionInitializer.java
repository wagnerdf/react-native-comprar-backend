package com.wagnerdf.comprar.config;

import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {

        log.info("Sincronizando permissões...");

        for (com.wagnerdf.comprar.enums.Permission permissionEnum :
                com.wagnerdf.comprar.enums.Permission.values()) {

            permissionRepository.findByName(permissionEnum.name())
                    .orElseGet(() -> {

                        Permission permission = new Permission();
                        permission.setName(permissionEnum.name());

                        log.info(
                                "Permissão criada: {}",
                                permissionEnum.name()
                        );

                        return permissionRepository.save(permission);
                    });
        }

        log.info("Permissões sincronizadas com sucesso.");
    }
}