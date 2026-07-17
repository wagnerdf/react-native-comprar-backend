package com.wagnerdf.comprar.dto.response;

import com.wagnerdf.comprar.enums.State;

import lombok.Builder;

@Builder
public record DeliveryAddressResponse(

        String recipientName,

        String zipCode,

        String street,

        String number,

        String complement,

        String neighborhood,

        String city,

        State state,

        String reference

) {}