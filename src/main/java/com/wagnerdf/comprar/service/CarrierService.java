package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.CarrierRequest;
import com.wagnerdf.comprar.dto.response.CarrierResponse;
import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.mapper.CarrierMapper;
import com.wagnerdf.comprar.repository.CarrierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarrierService {
	
	private final CarrierRepository carrierRepository;
	
	@Transactional
	public CarrierResponse create(CarrierRequest request) {

	    carrierRepository.findByNameIgnoreCase(request.name())
	            .ifPresent(carrier -> {
	                throw new BusinessException(
	                        "Carrier already exists.");
	            });

	    Carrier carrier =
	            Carrier.builder()
	                    .name(request.name())
	                    .active(true)
	                    .createdAt(LocalDateTime.now())
	                    .updatedAt(LocalDateTime.now())
	                    .build();

	    return CarrierMapper.toResponse(
	            carrierRepository.save(carrier));

	}

}
