package com.wagnerdf.comprar.dto.request;

import java.time.LocalDate;

import com.wagnerdf.comprar.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateEmployeeRequest {
	
	@NotBlank(message = "Nome é obrigatório")
    private String name;
	
	@Email(message = "Email inválido")
	@NotBlank(message = "Email é obrigatório")
    private String email;
	
	@NotNull(message = "Data de nacimento é obrigatório")
    private LocalDate birthDate;
	
	@NotNull(message = "Sexo é obrigatório")
    private Gender gender;

	@NotBlank(message = "Username é obrigatório")
    private String username;
	
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

}
