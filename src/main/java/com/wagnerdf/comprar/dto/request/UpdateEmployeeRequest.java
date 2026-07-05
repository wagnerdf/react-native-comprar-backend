package com.wagnerdf.comprar.dto.request;

import java.time.LocalDate;

import com.wagnerdf.comprar.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

	@Data
	public class UpdateEmployeeRequest {
	
	    @NotBlank(message = "Nome é obrigatório")
	    private String name;
	
	    @NotNull(message = "Data de nascimento é obrigatória")
	    private LocalDate birthDate;
	
	    @NotNull(message = "Sexo é obrigatório")
	    private Gender gender;

}
