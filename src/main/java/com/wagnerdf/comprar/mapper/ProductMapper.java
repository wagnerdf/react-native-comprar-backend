package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.request.CreateProductRequest;
import com.wagnerdf.comprar.entity.Product;

public class ProductMapper {
	
	private ProductMapper() {
    }

    public static Product toEntity(CreateProductRequest request) {

        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(request.getSku())
                .barcode(request.getBarcode())
                .price(request.getPrice())
                .stock(request.getStock())
                .minimumStock(request.getMinimumStock())
                .build();

    }

}
