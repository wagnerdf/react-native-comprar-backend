package com.wagnerdf.comprar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record FreightCalculationRequest(

        @NotBlank(message = "Shipping option ID is required.")
        String shippingOptionId,

        @NotBlank(message = "Destination ZIP code is required.")
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "Destination ZIP code must be in the format 00000-000 or 00000000."
        )
        String destinationZipCode

) {
}