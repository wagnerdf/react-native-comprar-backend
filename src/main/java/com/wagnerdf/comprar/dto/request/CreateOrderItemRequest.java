package com.wagnerdf.comprar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderItemRequest {

    @NotBlank(message = "Produto é obrigatório")
    private String productId;

    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantity;

}