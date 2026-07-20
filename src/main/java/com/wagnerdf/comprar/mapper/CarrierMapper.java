package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.response.CarrierResponse;
import com.wagnerdf.comprar.entity.Carrier;

public class CarrierMapper {

    private CarrierMapper() {
    }

    public static CarrierResponse toResponse(Carrier carrier) {

        return CarrierResponse.builder()
                .id(carrier.getId())
                .name(carrier.getName())
                .active(carrier.getActive())
                .build();

    }

}