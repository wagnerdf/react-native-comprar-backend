package com.wagnerdf.comprar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarrierRequest(

        @NotBlank(message = "O nome da transportadora é obrigatório..")
        @Size(max = 100)
        String name

) {
}