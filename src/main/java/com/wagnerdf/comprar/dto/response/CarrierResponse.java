package com.wagnerdf.comprar.dto.response;

import lombok.Builder;

@Builder
public record CarrierResponse(

        String id,

        String name,

        Boolean active

) {
}
