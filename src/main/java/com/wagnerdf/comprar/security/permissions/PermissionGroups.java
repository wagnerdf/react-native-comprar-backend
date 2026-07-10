package com.wagnerdf.comprar.security.permissions;

import java.util.HashSet;
import java.util.Set;

import com.wagnerdf.comprar.enums.Permission;

public final class PermissionGroups {

    private PermissionGroups() {
    }

    // =========================
    // PROFILE
    // =========================
    public static final PermissionGroup PROFILE =
            new PermissionGroup(Set.of(
                    Permission.READ_PROFILE
            ));

    // =========================
    // USER
    // =========================
    public static final PermissionGroup USER_FULL =
            new PermissionGroup(Set.of(
                    Permission.CREATE_USER,
                    Permission.READ_USER,
                    Permission.UPDATE_USER,
                    Permission.DELETE_USER
            ));

    // =========================
    // CATEGORY
    // =========================
    public static final PermissionGroup CATEGORY_FULL =
            new PermissionGroup(Set.of(
                    Permission.CREATE_CATEGORY,
                    Permission.READ_CATEGORY,
                    Permission.UPDATE_CATEGORY,
                    Permission.DELETE_CATEGORY,
                    Permission.REACTIVATE_CATEGORY
            ));

    // =========================
    // PRODUCT
    // =========================
    public static final PermissionGroup PRODUCT_FULL =
            new PermissionGroup(Set.of(
                    Permission.CREATE_PRODUCT,
                    Permission.READ_PRODUCT,
                    Permission.UPDATE_PRODUCT,
                    Permission.DELETE_PRODUCT,
                    Permission.REACTIVATE_PRODUCT
            ));

    // =========================
    // ORDER
    // =========================
    public static final PermissionGroup ORDER_FULL =
            new PermissionGroup(Set.of(
                    Permission.READ_ORDER,
                    Permission.UPDATE_ORDER,
                    Permission.CREATE_ORDER
            ));

    // =========================
    // EMPLOYEE
    // =========================
    public static final PermissionGroup EMPLOYEE_FULL =
            new PermissionGroup(Set.of(
                    Permission.CREATE_EMPLOYEE,
                    Permission.READ_EMPLOYEE,
                    Permission.UPDATE_EMPLOYEE,
                    Permission.DELETE_EMPLOYEE,
                    Permission.REACTIVATE_EMPLOYEE
            ));
    
	 // =========================
	 // USER SELF
	 // =========================
	 public static final PermissionGroup USER_SELF =
	         new PermissionGroup(Set.of(
	                 Permission.UPDATE_USER,
	                 Permission.DELETE_USER
	         ));
	
	 // =========================
	 // EMPLOYEE SELF
	 // =========================
	 public static final PermissionGroup EMPLOYEE_SELF =
	         new PermissionGroup(Set.of(
	                 Permission.UPDATE_USER
	         ));

    @SafeVarargs
    public static Set<Permission> merge(PermissionGroup... groups) {

        Set<Permission> permissions = new HashSet<>();

        for (PermissionGroup group : groups) {
            permissions.addAll(group.permissions());
        }

        return permissions;
    }

}