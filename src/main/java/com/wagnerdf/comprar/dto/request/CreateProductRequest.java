package com.wagnerdf.comprar.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CreateProductRequest {
	
	@NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotBlank(message = "SKU é obrigatório")
    private String sku;

    private String barcode;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "Estoque é obrigatório")
    @PositiveOrZero(message = "Estoque não pode ser negativo")
    private Integer stock;

    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
    private Integer minimumStock;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoryId;

}
