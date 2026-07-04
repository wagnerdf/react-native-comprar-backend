package com.wagnerdf.comprar.enums;

import java.util.Set;

public enum Role {

    USER(Set.of(
    		Permission.READ_PROFILE,
            Permission.UPDATE_USER,
            Permission.DELETE_USER
    )),
    
    EMPLOYEE(Set.of(

            Permission.READ_PROFILE,

            Permission.READ_CATEGORY,
            Permission.CREATE_CATEGORY,
            Permission.UPDATE_CATEGORY,
            Permission.DELETE_CATEGORY,
            Permission.REACTIVATE_CATEGORY,

            Permission.READ_PRODUCT,
            Permission.CREATE_PRODUCT,
            Permission.UPDATE_PRODUCT,
            Permission.DELETE_PRODUCT,
            Permission.REACTIVATE_PRODUCT,

            Permission.READ_ORDER,
            Permission.UPDATE_ORDER

    )),

    ADMIN(Set.of(
    		Permission.READ_PROFILE,

            Permission.CREATE_USER,
            Permission.READ_USER,
            Permission.UPDATE_USER,
            Permission.DELETE_USER,

            Permission.CREATE_CATEGORY,
            Permission.READ_CATEGORY,
            Permission.UPDATE_CATEGORY,
            Permission.DELETE_CATEGORY,
            Permission.REACTIVATE_CATEGORY,

            Permission.CREATE_PRODUCT,
            Permission.READ_PRODUCT,
            Permission.UPDATE_PRODUCT,
            Permission.DELETE_PRODUCT,
            Permission.REACTIVATE_PRODUCT,

            Permission.READ_ORDER,
            Permission.UPDATE_ORDER
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
