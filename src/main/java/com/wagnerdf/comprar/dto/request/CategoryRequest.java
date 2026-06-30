package com.wagnerdf.comprar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {
	
	@NotBlank(message = "Nome da categoria é obrigatório")
	@Size(max = 100, message = "Nome da categoria deve possuir no máximo 100 caracteres")
	private String name;
	
	@Size(max = 500, message = "Descrição deve possuir no máximo 500 caracteres")
	private String description;

}
