package com.wagnerdf.comprar.dto.request;

import com.wagnerdf.comprar.enums.State;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequest {
	
	@NotNull(message = "Username é obrigatório")
	private String nickname;

	@NotNull(message = "Nome do destinatário é obrigatório")
	private String recipientName;

	@NotNull(message = "O CEP é obrigatório")
	private String zipCode;

	@NotNull(message = "Rua é obrigatório")
	private String street;

	@NotNull(message = "Número do endereço é obrigatório")
	private String number;

	private String complement;

	private String neighborhood;

	@NotNull(message = "Cidade é obrigatório")
	private String city;

	@NotNull(message = "Estado é obrigatório")
	//@Size(min = 2, max = 2, message = "Estado precisar de 2 caracteres");
	private State state;

	private String reference;

}
