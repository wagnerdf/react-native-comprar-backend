package com.wagnerdf.comprar.service;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.FreightCalculationRequest;
import com.wagnerdf.comprar.dto.response.FreightCalculationResponse;
import com.wagnerdf.comprar.entity.ShippingOption;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.ShippingOptionNotFoundException;
import com.wagnerdf.comprar.repository.ShippingOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreightService {

    private final ShippingOptionRepository shippingOptionRepository;

    // ================================================================================
    // ----------------Cálculo de Frete----------------
    // 🎯 Regras
    // ✅ ShippingOption deve existir.
    // ✅ ShippingOption deve estar ativa.
    // ✅ Carrier deve estar ativo.
    // ✅ Retorna o preço cadastrado na ShippingOption.
    // ✅ Retorna o prazo estimado cadastrado na ShippingOption.
    // ================================================================================

    public FreightCalculationResponse calculate(
            FreightCalculationRequest request) {

        ShippingOption option = shippingOptionRepository
                .findById(request.shippingOptionId())
                .orElseThrow(() ->
                        new ShippingOptionNotFoundException(
                                request.shippingOptionId()));

        if (!option.getActive()) {
            throw new BusinessException(
                    "Shipping option is inactive.");
        }

        if (!option.getCarrier().getActive()) {
            throw new BusinessException(
                    "Carrier is inactive.");
        }

        return new FreightCalculationResponse(
                option.getId(),
                option.getCarrier().getName(),
                option.getServiceName(),
                option.getPrice(),
                option.getEstimatedDays()
        );
    }
}