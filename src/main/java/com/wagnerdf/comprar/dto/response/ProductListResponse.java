package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductListResponse {
	
	private String id;

    private String name;

    private String sku;

    private BigDecimal price;

    private Integer stock;

    private Boolean active;

    private String category;

}
