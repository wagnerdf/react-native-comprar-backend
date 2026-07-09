package com.wagnerdf.comprar.dto.request;

import com.wagnerdf.comprar.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status é obrigatório")
    private OrderStatus status;

}