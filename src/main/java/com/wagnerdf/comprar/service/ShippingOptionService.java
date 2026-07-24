package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.ShippingOptionRequest;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.entity.ShippingOption;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.CarrierNotFoundException;
import com.wagnerdf.comprar.mapper.ShippingOptionMapper;
import com.wagnerdf.comprar.repository.CarrierRepository;
import com.wagnerdf.comprar.repository.ShippingOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingOptionService {

    private final ShippingOptionRepository shippingOptionRepository;
    private final CarrierRepository carrierRepository;

    /**
     * ==========================================================
     * FIND CARRIER
     * ==========================================================
     */
    private Carrier findByIdOrThrow(String id) {

        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() ->
                        new CarrierNotFoundException(id));

        if (!carrier.getActive()) {
            throw new BusinessException(
                    "Carrier is inactive.");
        }

        return carrier;

    }

    /**
     * ==========================================================
     * CREATE SHIPPING OPTION
     * ==========================================================
     */
    @Transactional
    public ShippingOptionResponse create(
            ShippingOptionRequest request) {

        Carrier carrier =
                findByIdOrThrow(request.carrierId());

        String serviceName =
                request.serviceName().trim();

        shippingOptionRepository
                .findByCarrierIdAndServiceNameIgnoreCase(
                        carrier.getId(),
                        serviceName)
                .ifPresent(option -> {
                    throw new BusinessException(
                            "Shipping service already exists for this carrier.");
                });

        ShippingOption option =
                ShippingOption.builder()
                        .carrier(carrier)
                        .serviceName(serviceName)
                        .price(request.price())
                        .estimatedDays(request.estimatedDays())
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        option = shippingOptionRepository.save(option);

        return ShippingOptionMapper.toResponse(option);

    }

}