package com.wagnerdf.comprar.security.permissions;

import java.util.Set;

import com.wagnerdf.comprar.enums.Permission;

public record PermissionGroup(Set<Permission> permissions) {
	
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

}