package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;

public record FreightCalculationResponse(
        String shippingOptionId,
        String carrierName,
        String serviceName,
        BigDecimal price,
        Integer estimatedDays
) {
}