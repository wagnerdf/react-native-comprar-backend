package com.wagnerdf.comprar.enums;

import java.util.Set;

import com.wagnerdf.comprar.security.permissions.PermissionGroups;

public enum Role {

	USER(
		    PermissionGroups.merge(
		        PermissionGroups.PROFILE,
		        PermissionGroups.USER_SELF
		    )
		),

		EMPLOYEE(
		    PermissionGroups.merge(
		        PermissionGroups.PROFILE,
		        PermissionGroups.CATEGORY_FULL,
		        PermissionGroups.PRODUCT_FULL,
		        PermissionGroups.ORDER_FULL,
		        PermissionGroups.EMPLOYEE_SELF
		    )
		),

		ADMIN(
		    PermissionGroups.merge(
		        PermissionGroups.PROFILE,
		        PermissionGroups.USER_FULL,
		        PermissionGroups.CATEGORY_FULL,
		        PermissionGroups.PRODUCT_FULL,
		        PermissionGroups.ORDER_FULL,
		        PermissionGroups.EMPLOYEE_FULL
		    )
		);

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
