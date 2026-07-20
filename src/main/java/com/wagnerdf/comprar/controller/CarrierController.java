package com.wagnerdf.comprar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.CarrierRequest;
import com.wagnerdf.comprar.dto.response.CarrierResponse;
import com.wagnerdf.comprar.service.CarrierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/carriers")
@RequiredArgsConstructor
public class CarrierController {
	
	private final CarrierService carrierService;
	
	@PostMapping
	public ResponseEntity<CarrierResponse> create(
	        @Valid
	        @RequestBody CarrierRequest request) {

	    CarrierResponse response =
	            carrierService.create(request);

	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(response);

	}

}
