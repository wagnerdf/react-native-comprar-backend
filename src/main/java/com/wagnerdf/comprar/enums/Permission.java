package com.wagnerdf.comprar.enums;

public enum Permission {
	
    // =========================
    // PERFIL
    // =========================
    READ_PROFILE,

    // =========================
    // USUÁRIOS
    // =========================
    CREATE_USER,
    READ_USER,
    UPDATE_USER,
    DELETE_USER,

    // =========================
    // CATEGORIAS
    // =========================
    CREATE_CATEGORY,
    READ_CATEGORY,
    UPDATE_CATEGORY,
    DELETE_CATEGORY,
    REACTIVATE_CATEGORY,

    // =========================
    // PRODUTOS
    // =========================
    CREATE_PRODUCT,
    READ_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,
    REACTIVATE_PRODUCT,

    // =========================
    // PEDIDOS
    // =========================
    READ_ORDER,
    UPDATE_ORDER,
    CREATE_ORDER,
    
    // =========================
    // EMPLOYEE
    // =========================
    CREATE_EMPLOYEE,
    READ_EMPLOYEE,
    UPDATE_EMPLOYEE,
    DELETE_EMPLOYEE,
    REACTIVATE_EMPLOYEE,
}
