package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.response.ShippingOptionListResponse;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.entity.ShippingOption;

public class ShippingOptionMapper {
	
	public static ShippingOptionResponse toResponse(
	        ShippingOption option) {

	    return ShippingOptionResponse.builder()
	            .id(option.getId())
	            .carrierId(option.getCarrier().getId())
	            .carrierName(option.getCarrier().getName())
	            .serviceName(option.getServiceName())
	            .price(option.getPrice())
	            .estimatedDays(option.getEstimatedDays())
	            .active(option.getActive())
	            .build();

	}
	
	public static ShippingOptionListResponse toListResponse(
	        ShippingOption option) {

	    return ShippingOptionListResponse.builder()
	            .id(option.getId())
	            .carrierName(option.getCarrier().getName())
	            .serviceName(option.getServiceName())
	            .price(option.getPrice())
	            .estimatedDays(option.getEstimatedDays())
	            .active(option.getActive())
	            .build();

	}

}
