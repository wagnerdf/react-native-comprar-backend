package com.wagnerdf.comprar.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FreightCalculationRequest(

        @NotBlank(message = "Shipping option ID is required.")
        String shippingOptionId,

        @NotBlank(message = "Destination ZIP code is required.")
        String destinationZipCode

) {
}