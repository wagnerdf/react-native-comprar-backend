package com.wagnerdf.comprar.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.FreightCalculationRequest;
import com.wagnerdf.comprar.dto.response.CepResponse;
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
    private final CepService cepService;
	
	 // ================================================================================
	 // ----------------Cálculo de Frete----------------
	 // 🎯 Regras
	 // ✅ ShippingOption deve existir.
	 // ✅ ShippingOption deve estar ativa.
	 // ✅ Carrier deve estar ativo.
	 // ✅ CEP de origem deve existir.
	 // ✅ CEP de destino deve existir.
	 // ✅ Mesma UF utiliza o preço base.
	 // ✅ UF diferente aplica adicional regional.
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
	
	     CepResponse origin =
	             cepService.findByCep(
	                     option.getOriginZipCode());
	
	     CepResponse destination =
	             cepService.findByCep(
	                     request.destinationZipCode());
	
	     BigDecimal finalPrice = option.getPrice();
	
	     if (!origin.uf().equalsIgnoreCase(destination.uf())) {
	
	         finalPrice = finalPrice.add(
	                 BigDecimal.TEN
	         );
	     }
	
	     return new FreightCalculationResponse(
	             option.getId(),
	             option.getCarrier().getName(),
	             option.getServiceName(),
	             finalPrice,
	             option.getEstimatedDays()
	     );
	 }
}