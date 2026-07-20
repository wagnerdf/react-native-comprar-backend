package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.CarrierRequest;
import com.wagnerdf.comprar.dto.response.CarrierResponse;
import com.wagnerdf.comprar.entity.Carrier;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.CarrierNotFoundException;
import com.wagnerdf.comprar.mapper.CarrierMapper;
import com.wagnerdf.comprar.repository.CarrierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarrierService {
	
	private final CarrierRepository carrierRepository;
	
	@Transactional
	public CarrierResponse create(CarrierRequest request) {
		
		String carrierName = request.name().trim();
	    carrierRepository.findByNameIgnoreCase(carrierName)
	            .ifPresent(carrier -> {
	                throw new BusinessException(
	                        "Carrier already exists.");
	            });
	    
	    Carrier carrier =
	            Carrier.builder()
	            		.name(carrierName)
	                    .active(true)
	                    .createdAt(LocalDateTime.now())
	                    .updatedAt(LocalDateTime.now())
	                    .build();

	    return CarrierMapper.toResponse(
	            carrierRepository.save(carrier));

	}
	
	private Carrier findCarrier(String id) {

	    return carrierRepository.findById(id)
	            .orElseThrow(() ->
	                    new CarrierNotFoundException(id));

	}
	
	
	
	@Transactional(readOnly = true)
	public List<CarrierResponse> findAll() {

	    return carrierRepository.findAll()
	            .stream()
	            .map(CarrierMapper::toResponse)
	            .toList();

	}
	
	@Transactional(readOnly = true)
	public CarrierResponse findById(String id) {

	    return CarrierMapper.toResponse(
	            findCarrier(id));

	}
	
	@Transactional
	public CarrierResponse update(
	        String id,
	        CarrierRequest request) {

	    Carrier carrier = findCarrier(id);

	    String carrierName = request.name().trim();

	    carrierRepository.findByNameIgnoreCase(carrierName)
	            .ifPresent(existing -> {
	                if (!existing.getId().equals(id)) {
	                    throw new BusinessException(
	                            "Carrier already exists.");
	                }
	            });

	    carrier.setName(carrierName);
	    carrier.setUpdatedAt(LocalDateTime.now());

	    return CarrierMapper.toResponse(
	            carrierRepository.save(carrier));
	}
	
	@Transactional
	public void delete(String id) {

	    Carrier carrier =
	            findCarrier(id);

	    carrier.setActive(false);
	    carrier.setUpdatedAt(LocalDateTime.now());

	    carrierRepository.save(carrier);

	}
	
	@Transactional
	public CarrierResponse reactivate(String id) {

	    Carrier carrier =
	            findCarrier(id);

	    carrier.setActive(true);
	    carrier.setUpdatedAt(LocalDateTime.now());

	    return CarrierMapper.toResponse(
	            carrierRepository.save(carrier));

	}

}
