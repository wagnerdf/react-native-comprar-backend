package com.wagnerdf.comprar.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ShippingOptionUpdateRequest(

        @NotBlank
        String serviceName,

        @PositiveOrZero
        BigDecimal price,

        @Positive
        Integer estimatedDays

) {}
