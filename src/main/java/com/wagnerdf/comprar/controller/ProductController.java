package com.wagnerdf.comprar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.comprar.dto.request.CreateProductRequest;
import com.wagnerdf.comprar.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    public ResponseEntity<Void> create(
            @Valid @RequestBody CreateProductRequest request
    ) {

        productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
