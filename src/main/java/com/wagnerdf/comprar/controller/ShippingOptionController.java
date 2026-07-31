package com.wagnerdf.comprar.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.ShippingOptionRequest;
import com.wagnerdf.comprar.dto.request.ShippingOptionUpdateRequest;
import com.wagnerdf.comprar.dto.response.ShippingOptionListResponse;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.dto.response.SuccessResponse;
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
	
	@GetMapping("/{id}")
	public ResponseEntity<ShippingOptionResponse> findById(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.findById(id));

	}
	
	@GetMapping
	public ResponseEntity<Page<ShippingOptionListResponse>> findAll(
	        Pageable pageable) {

	    return ResponseEntity.ok(
	            shippingOptionService.findAll(pageable));

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ShippingOptionResponse> update(

	        @PathVariable
	        String id,

	        @Valid
	        @RequestBody
	        ShippingOptionUpdateRequest request) {

	    return ResponseEntity.ok(
	            shippingOptionService.update(id, request));

	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_SHIPPING_OPTION')")
	public ResponseEntity<SuccessResponse> delete(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.delete(id));

	}

}
