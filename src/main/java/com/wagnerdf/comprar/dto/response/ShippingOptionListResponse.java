package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ShippingOptionListResponse(

        String id,

        String carrierName,

        String serviceName,

        BigDecimal price,

        Integer estimatedDays,

        Boolean active

) {}
