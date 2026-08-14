package com.wagnerdf.comprar.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ShippingOptionUpdateRequest(

		 @NotBlank(message = "Service name is required")
        String serviceName,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be greater than or equal to zero.")
        BigDecimal price,

        @NotNull(message = "Estimated days is required.")
        @Positive(message = "Estimated days must be greater than zero.")
        Integer estimatedDays,
        
        @NotBlank(message = "Origin ZIP code is required.")
        @Pattern(
                regexp = "\\d{5}-?\\d{3}",
                message = "Origin ZIP code must be in the format 00000-000 or 00000000."
        )
        String originZipCode

) {}
