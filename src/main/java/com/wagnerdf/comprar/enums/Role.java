package com.wagnerdf.comprar.enums;

import java.util.Set;

public enum Role {

    USER(Set.of(
    		Permission.READ_PROFILE,
            Permission.UPDATE_USER,
            Permission.DELETE_USER
    )),

    ADMIN(Set.of(
    		Permission.READ_PROFILE,
    		Permission.READ_USER,
            Permission.CREATE_USER,
            Permission.UPDATE_USER,
            Permission.DELETE_USER
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
