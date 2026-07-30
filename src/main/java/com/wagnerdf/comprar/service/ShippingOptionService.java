package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.ShippingOptionRequest;
import com.wagnerdf.comprar.dto.request.ShippingOptionUpdateRequest;
import com.wagnerdf.comprar.dto.response.ShippingOptionListResponse;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.entity.ShippingOption;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.CarrierNotFoundException;
import com.wagnerdf.comprar.exception.ShippingOptionNotFoundException;
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
     * FIND CARRIER BY ID
     * ==========================================================
     *
     * Localiza uma transportadora pelo ID.
     *
     * Regras:
     *
     * - A transportadora deve existir.
     * - A transportadora deve estar ativa.
     *
     * Lança:
     *
     * - CarrierNotFoundException
     * - BusinessException (quando estiver inativa)
     *
     */
    private Carrier findCarrierByIdOrThrow(String id) {

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
        		findCarrierByIdOrThrow(request.carrierId());

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
    
    /**
     * ==========================================================
     * FIND SHIPPING OPTION BY ID
     * ==========================================================
     *
     * Localiza uma opção de frete pelo ID.
     *
     * Regras:
     *
     * - A opção de frete deve existir.
     *
     * Lança:
     *
     * - ShippingOptionNotFoundException
     *
     */
    private ShippingOption findShippingOptionByIdOrThrow(String id) {

        return shippingOptionRepository.findById(id)
                .orElseThrow(() ->
                        new ShippingOptionNotFoundException(id));

    }
    
    @Transactional(readOnly = true)
    public ShippingOptionResponse findById(String id) {

    	ShippingOption option =
    	        findShippingOptionByIdOrThrow(id);

        return ShippingOptionMapper.toResponse(option);

    }
    
    /**
     * ==========================================================
     * FIND ALL SHIPPING OPTIONS
     * ==========================================================
     *
     * Retorna uma lista paginada das opções de frete.
     *
     * A paginação é realizada automaticamente pelo Spring através
     * do objeto Pageable.
     *
     * Exemplos:
     *
     * GET /shipping-options
     *
     * GET /shipping-options?page=0&size=10
     *
     * GET /shipping-options?page=1&size=20
     *
     * Ordenação:
     *
     * GET /shipping-options?sort=serviceName,asc
     *
     * GET /shipping-options?sort=price,desc
     *
     * Também é possível combinar:
     *
     * GET /shipping-options?page=0&size=10&sort=price,asc
     *
     * Retorno:
     *
     * - Lista paginada de ShippingOptionListResponse.
     *
     */
    @Transactional(readOnly = true)
    public Page<ShippingOptionListResponse> findAll(
            Pageable pageable) {

        return shippingOptionRepository
                .findAll(pageable)
                .map(ShippingOptionMapper::toListResponse);

    }
    
    /**
     * ==========================================================
     * UPDATE SHIPPING OPTION
     * ==========================================================
     *
     * Atualiza uma opção de frete.
     *
     * Regras:
     *
     * - A opção deve existir.
     * - O nome do serviço é normalizado.
     * - Não permite serviços duplicados na mesma transportadora.
     * - Atualiza updatedAt.
     *
     */
    @Transactional
    public ShippingOptionResponse update(
            String id,
            ShippingOptionUpdateRequest request) {

        ShippingOption option =
                findShippingOptionByIdOrThrow(id);

        String serviceName =
                request.serviceName().trim();

        if (shippingOptionRepository
                .existsByCarrierIdAndServiceNameIgnoreCaseAndIdNot(
                        option.getCarrier().getId(),
                        serviceName,
                        option.getId())) {

            throw new BusinessException(
                    "Shipping service already exists for this carrier.");
        }

        option.setServiceName(serviceName);
        option.setPrice(request.price());
        option.setEstimatedDays(request.estimatedDays());
        option.setUpdatedAt(LocalDateTime.now());

        return ShippingOptionMapper.toResponse(
                shippingOptionRepository.save(option));

    }

}