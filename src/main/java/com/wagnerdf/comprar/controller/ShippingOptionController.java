package com.wagnerdf.comprar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.ShippingOptionRequest;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.service.ShippingOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RestController
@RequestMapping("/shipping-options")
@RequiredArgsConstructor
public class ShippingOptionController {
	
	private final ShippingOptionService shippingOptionService;
	
	@PostMapping
	public ResponseEntity<ShippingOptionResponse> create(

	        @Valid
	        @RequestBody
	        ShippingOptionRequest request) {

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(shippingOptionService.create(request));

	}

}
