package com.wagnerdf.comprar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.comprar.dto.request.AddressRequest;
import com.wagnerdf.comprar.dto.response.AddressResponse;
import com.wagnerdf.comprar.service.AddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> create(
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressService.create(request));
    }
    
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> findAll() {

        return ResponseEntity.ok(addressService.findAll());
    }
    
    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressResponse> update(
            @PathVariable String id,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.update(id, request)
        );
    }
    
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id) {

        addressService.delete(id);

        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/addresses/{id}/default")
    public ResponseEntity<AddressResponse> setDefault(
            @PathVariable String id) {

        return ResponseEntity.ok(
                addressService.setDefault(id)
        );
    }
}
