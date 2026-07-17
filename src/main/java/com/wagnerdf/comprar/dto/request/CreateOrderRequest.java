package com.wagnerdf.comprar.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotEmpty(message = "O pedido deve possuir pelo menos um item")
    @Valid
    private List<CreateOrderItemRequest> items;
    
    private String addressId;

}