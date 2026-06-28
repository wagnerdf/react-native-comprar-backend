package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.AddressRequest;
import com.wagnerdf.comprar.dto.response.AddressResponse;
import com.wagnerdf.comprar.entity.Address;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.mapper.AddressMapper;
import com.wagnerdf.comprar.repository.AddressRepository;
import com.wagnerdf.comprar.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final AuditService auditService;

    // ==================================================================================
    // ----------------Cadastro de endereço------------------
    // 🎯 VALIDAÇÕES
    // ✅ Apenas o usuário autenticado pode cadastrar seus próprios endereços.
    // ✅ O userId nunca será enviado pelo cliente.
    // ✅ Máximo de 10 endereços por usuário.
    // ✅ O primeiro endereço cadastrado será automaticamente o endereço padrão.
    // ✅ Apenas um endereço pode ser marcado como padrão por vez.
    // ✅ Endereços poderão ser editados e removidos posteriormente.
    // ==================================================================================
    public AddressResponse create(AddressRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        long totalAddresses = addressRepository.countByUser(user);

        if (totalAddresses >= 10) {
            throw new BusinessException(
                    "Limite máximo de 10 endereços atingido.");
        }

        Address address = AddressMapper.toEntity(request);

        address.setUser(user);

        address.setDefaultAddress(totalAddresses == 0);

        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());

        Address savedAddress = addressRepository.save(address);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "CREATE_ADDRESS"
        );

        return AddressMapper.toResponse(savedAddress);
    }

}
