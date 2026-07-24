package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ShippingOptionResponse(

        String id,

        String carrierId,

        String carrierName,

        String serviceName,

        BigDecimal price,

        Integer estimatedDays,

        Boolean active

) {}
