package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.request.AddressRequest;
import com.wagnerdf.comprar.dto.response.AddressResponse;
import com.wagnerdf.comprar.entity.Address;

public class AddressMapper {
	
	public static Address toEntity(AddressRequest request) {

        return Address.builder()
                .nickname(request.getNickname())
                .recipientName(request.getRecipientName())
                .zipCode(request.getZipCode())
                .street(request.getStreet())
                .number(request.getNumber())
                .complement(request.getComplement())
                .neighborhood(request.getNeighborhood())
                .city(request.getCity())
                .state(request.getState())
                .reference(request.getReference())
                .build();
    }

    public static AddressResponse toResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .nickname(address.getNickname())
                .recipientName(address.getRecipientName())
                .zipCode(address.getZipCode())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .reference(address.getReference())
                .defaultAddress(address.getDefaultAddress())
                .build();
    }

}
