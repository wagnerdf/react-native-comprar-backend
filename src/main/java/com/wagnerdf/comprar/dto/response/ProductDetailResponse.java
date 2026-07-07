package com.wagnerdf.comprar.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailResponse {

    private String id;

    private String name;

    private String description;

    private String sku;

    private String barcode;

    private BigDecimal price;

    private Integer stock;

    private Integer minimumStock;

    private Boolean active;

    private String categoryId;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}