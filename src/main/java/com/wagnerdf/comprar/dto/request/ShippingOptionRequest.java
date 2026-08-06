package com.wagnerdf.comprar.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ShippingOptionRequest(

		@NotBlank(message = "Carrier is required.")
        String carrierId,

        @NotBlank
        String serviceName,

        @PositiveOrZero
        BigDecimal price,

        @Positive
        Integer estimatedDays

) {}
