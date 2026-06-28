package com.wagnerdf.comprar.dto.response;

import com.wagnerdf.comprar.enums.State;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
	
	private String id;

	private String nickname;

	private String recipientName;

	private String zipCode;

	private String street;

	private String number;

	private String complement;

	private String neighborhood;

	private String city;

	private State state;

	private String reference;

	private Boolean defaultAddress;

}
