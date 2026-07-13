package com.wagnerdf.comprar.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("Aguardando processamento"),

    PROCESSING("Em processamento"),

    PAID("Pagamento aprovado"),

    SHIPPED("Pedido enviado"),

    DELIVERED("Pedido entregue"),

    CANCELLED("Pedido cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
