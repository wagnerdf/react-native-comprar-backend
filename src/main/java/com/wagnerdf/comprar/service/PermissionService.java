package com.wagnerdf.comprar.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.entity.Permission;
import com.wagnerdf.comprar.enums.Role;
import com.wagnerdf.comprar.repository.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Set<Permission> getPermissionsByRole(Role role) {

        return role.getPermissions()
                .stream()
                .map(permissionEnum ->
                        permissionRepository.findByName(permissionEnum.name())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Permissão não encontrada: "
                                                        + permissionEnum.name()
                                        )))
                .collect(Collectors.toSet());
    }
}