package com.wagnerdf.comprar.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.FreightCalculationRequest;
import com.wagnerdf.comprar.dto.response.FreightCalculationResponse;
import com.wagnerdf.comprar.service.FreightService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/freight")
@RequiredArgsConstructor
public class FreightController {

    private final FreightService freightService;

    // ================================================================================
    // ----------------Cálculo de Frete----------------
    // 🎯 Regras
    // ✅ Recebe a ShippingOption escolhida.
    // ✅ Recebe o CEP de destino.
    // ✅ Valida o ID da ShippingOption.
    // ✅ Valida o formato do CEP.
    // ✅ Valida se a ShippingOption existe.
    // ✅ Valida se a ShippingOption está ativa.
    // ✅ Valida se a Carrier está ativa.
    // ✅ Retorna preço e prazo estimado.
    // ⚠️ O CEP ainda não influencia o cálculo nesta primeira versão.
    // ================================================================================

    @PostMapping("/calculate")
    public ResponseEntity<FreightCalculationResponse> calculate(
            @Valid
            @RequestBody
            FreightCalculationRequest request) {

        return ResponseEntity.ok(
                freightService.calculate(request)
        );
    }
}