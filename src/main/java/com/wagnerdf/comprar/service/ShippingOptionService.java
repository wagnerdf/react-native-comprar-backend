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
import com.wagnerdf.comprar.dto.response.SuccessResponse;
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
    private final AuditService auditService;
    private final AuthenticatedUserService authenticatedUserService;

	// ================================================================================
	// ----------------Busca de Carrier----------------
	// Localiza uma transportadora válida pelo ID.
    //
    // Regras:
    //
    // - A transportadora deve existir.
    // - A transportadora deve estar ativa.
    //
    // Lança:
    //
    // - CarrierNotFoundException
    // - BusinessException (quando estiver inativa)
    //
	// ================================================================================
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

	 // ================================================================================
	 // ----------------Cadastro de Opção de Frete----------------
	 // 🎯 Regras
	 // ✅ Carrier deve existir.
	 // ✅ Carrier deve estar ativo.
	 // ✅ Nome do serviço obrigatório.
	 // ✅ Remove espaços no início e fim do nome.
	 // ✅ Não permite duplicidade de serviço para a mesma transportadora.
	 // ✅ Opção de frete nasce ativa.
	 // ✅ createdAt recebe data atual.
	 // ✅ updatedAt recebe data atual.
	 // ✅ Registrar auditoria (CREATE_SHIPPING_OPTION).
	 // ================================================================================
    @Transactional
    public ShippingOptionResponse create(
            ShippingOptionRequest request) {

        Carrier carrier =
        		findCarrierByIdOrThrow(request.carrierId());

        String serviceName =
                request.serviceName().trim();
        
        String originZipCode =
                request.originZipCode()
                        .replaceAll("\\D", "");

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
                        .originZipCode(originZipCode)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        option = shippingOptionRepository.save(option);
        
        ShippingOption saved =
                shippingOptionRepository.save(option);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "CREATE_SHIPPING_OPTION"
        );

        return ShippingOptionMapper.toResponse(saved);

    }
    
	// ================================================================================
	// ----------------Busca de ShippingOption----------------
	// Localiza uma opção de frete pelo ID.
    //
    // Localiza uma opção de frete pelo ID.
    //
    // Regras:
    //
    // - A opção de frete deve existir.
    //
    // Lança:
    //
    // - ShippingOptionNotFoundException
    //
	// ================================================================================
    private ShippingOption findShippingOptionByIdOrThrow(String id) {

        return shippingOptionRepository.findById(id)
                .orElseThrow(() ->
                        new ShippingOptionNotFoundException(id));

    }
    
	 // ================================================================================
	 // ----------------Consulta por ID----------------
	 // 🎯 Regras
	 // ✅ Localiza a opção de frete pelo ID.
	 // ✅ Lança ShippingOptionNotFoundException quando inexistente.
	 // ================================================================================
    @Transactional(readOnly = true)
    public ShippingOptionResponse findById(String id) {

    	ShippingOption option =
    	        findShippingOptionByIdOrThrow(id);

        return ShippingOptionMapper.toResponse(option);

    }
    
    
	 // ================================================================================
	 // ----------------Listagem Paginada----------------
	 // 🎯 Regras
	 // ✅ Retorna todas as opções de frete.
	 // ✅ Suporta paginação.
	 // ✅ Suporta ordenação.
     //
     // Retorna uma lista paginada das opções de frete.
     //
     // A paginação é realizada automaticamente pelo Spring através
     // do objeto Pageable.
     //
     // Exemplos:
     //
     // GET /shipping-options
     //
     // GET /shipping-options?page=0&size=10
     //
     // GET /shipping-options?page=1&size=20
     //
     // Ordenação:
     //
     // GET /shipping-options?sort=serviceName,asc
     //
     // GET /shipping-options?sort=price,desc
     //
     // Também é possível combinar:
     //
     // GET /shipping-options?page=0&size=10&sort=price,asc
     //
     // Retorno:
     //
     // - Lista paginada de ShippingOptionListResponse.
     //
     // ================================================================================
    @Transactional(readOnly = true)
    public Page<ShippingOptionListResponse> findAll(
            Pageable pageable) {

        return shippingOptionRepository
                .findAll(pageable)
                .map(ShippingOptionMapper::toListResponse);

    }
    
	 // ================================================================================
	 // ----------------Atualização----------------
	 // 🎯 Regras
	 // ✅ Opção de frete deve existir.
	 // ✅ Não permite duplicidade de serviço para a mesma transportadora.
	 // ✅ Atualiza serviceName.
	 // ✅ Atualiza price.
	 // ✅ Atualiza estimatedDays.
	 // ✅ updatedAt recebe data atual.
	 // ✅ Registrar auditoria (UPDATE_SHIPPING_OPTION).
	 // ================================================================================
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
        option.setOriginZipCode(
                request.originZipCode()
                        .replaceAll("\\D", "")
        );
        
        ShippingOption saved = shippingOptionRepository.save(option);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "UPDATE_SHIPPING_OPTION"
        );

        return ShippingOptionMapper.toResponse(saved);

    }
    
	 // ================================================================================
	 // ----------------Soft Delete----------------
	 // 🎯 Regras
	 // ✅ Opção de frete deve existir.
	 // ✅ Não permite excluir registro já inativo.
	 // ✅ active = false.
	 // ✅ updatedAt recebe data atual.
	 // ✅ Registrar auditoria (DELETE_SHIPPING_OPTION).
	 // ================================================================================
    @Transactional
    public SuccessResponse delete(String id) {

        ShippingOption option =
                findShippingOptionByIdOrThrow(id);

        if (!option.getActive()) {
            throw new BusinessException(
                    "Shipping option is already inactive.");
        }

        option.setActive(false);
        option.setUpdatedAt(LocalDateTime.now());

        shippingOptionRepository.save(option);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "DELETE_SHIPPING_OPTION"
        );

        return SuccessResponse.builder()
                .message("Shipping option deleted successfully.")
                .build();

    }
    
	// ================================================================================
	// ----------------Reativação----------------
	// 🎯 Regras
	// ✅ Opção de frete deve existir.
	// ✅ Não permite reativar registro já ativo.
	// ✅ active = true.
	// ✅ updatedAt recebe data atual.
	// ✅ Registrar auditoria (REACTIVATE_SHIPPING_OPTION).
    //
    // Fluxo:
    //
    // Localiza a opção de frete
    //        ↓
    // Valida se já está ativa
    //        ↓
    // Ativa a opção
    //        ↓
    // Atualiza updatedAt
    //        ↓
    // Salva no banco
    //        ↓
    // Retorna ShippingOptionResponse
    //
    // @param id Identificador da opção de frete.
    // @return ShippingOptionResponse contendo os dados atualizados.
	 // ================================================================================
    @Transactional
    public ShippingOptionResponse reactivate(String id) {

        ShippingOption option = findShippingOptionByIdOrThrow(id);

        if (option.getActive()) {
            throw new BusinessException(
                    "Shipping option is already active.");
        }

        option.setActive(true);
        option.setUpdatedAt(LocalDateTime.now());
        
        ShippingOption saved =
                shippingOptionRepository.save(option);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "REACTIVATE_SHIPPING_OPTION"
        );

        return ShippingOptionMapper.toResponse(saved);

    }
}