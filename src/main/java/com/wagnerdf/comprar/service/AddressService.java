package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.AddressRequest;
import com.wagnerdf.comprar.dto.response.AddressResponse;
import com.wagnerdf.comprar.entity.Address;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.mapper.AddressMapper;
import com.wagnerdf.comprar.repository.AddressRepository;

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
    
    // ==================================================================================
    // ----------------Listagem de endereço------------------
    // 🎯 Regras
    // ✅ Retornar apenas os endereços do usuário autenticado.
    // ✅ Não receber userId na URL nem como parâmetro.
    // ✅ Utilizar o AuthenticatedUserService.
    // ✅ Retornar uma lista de AddressResponse.
    // ❌ Sem paginação (como tem 10 endereços, a paginação não sera aplicada).
    // ❌ Não registrar em audit_logs.
    // ==================================================================================
    public List<AddressResponse> findAll() {

        User user = authenticatedUserService.getCurrentUser();

        return addressRepository.findByUserOrderByDefaultAddressDescCreatedAtAsc(user)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
    }
    
    // ==================================================================================
    // ----------------Edição de endereço------------------
    // 🎯 Regras
    // ✅ Apenas o dono do endereço pode alterá-lo.
    // ✅ O id virá pela URL.
    // ✅ O usuário não poderá alterar o defaultAddress pelo PUT.
    // ✅ O usuário não poderá trocar o endereço de dono (user).
    // ✅ createdAt permanece inalterado.
    // ✅ updatedAt será atualizado automaticamente.
    // ✅ Registrar auditoria (UPDATE_ADDRESS).
    // ✅ Retornar 404 caso o endereço não exista.
    // ✅ Retornar 403 caso tente editar endereço de outro usuário.
    // ==================================================================================
    public AddressResponse update(String id, AddressRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        Address address = addressRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new BusinessException("Endereço não encontrado."));

        AddressMapper.updateEntity(address, request);

        address.setUpdatedAt(LocalDateTime.now());

        Address saved = addressRepository.save(address);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "UPDATE_ADDRESS"
        );

        return AddressMapper.toResponse(saved);
    }
    
	 // ==================================================================================
	 // --------Busca endereço pelo ID e usuário autenticado---------
	 // 🎯 Objetivo
	 // ✅ Buscar um endereço pelo ID.
	 // ✅ Garantir que o endereço pertença ao usuário autenticado.
	 // ✅ Lançar BusinessException caso o endereço não seja encontrado.
	 // ==================================================================================
    private Address findAddress(String id, User user) {

        return addressRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new BusinessException("Endereço não encontrado."));
    }
    
	 // ==================================================================================
	 // ----------------Exclusão de endereço------------------
	 // 🎯 Regras
	 // ✅ Apenas o dono do endereço pode excluí-lo.
	 // ✅ O id virá pela URL.
	 // ✅ Registrar auditoria (DELETE_ADDRESS).
	 // ✅ Se o endereço for padrão, promover automaticamente o último endereço cadastrado.
	 // ✅ Se não existir outro endereço, nenhum ficará como padrão.
	 // ✅ Retornar 404 caso o endereço não exista.
	 // ✅ Retornar 403 caso tente excluir endereço de outro usuário.
	 // ==================================================================================

    public void delete(String id) {

        User user = authenticatedUserService.getCurrentUser();

        Address address = findAddress(id, user);

        boolean wasDefault = address.getDefaultAddress();

        addressRepository.delete(address);

        if (wasDefault) {

            addressRepository
                    .findFirstByUserOrderByCreatedAtDesc(user)
                    .ifPresent(lastAddress -> {

                        lastAddress.setDefaultAddress(true);

                        lastAddress.setUpdatedAt(LocalDateTime.now());

                        addressRepository.save(lastAddress);

                    });
        }

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "DELETE_ADDRESS"
        );
    }
    
	 // ==================================================================================
	 // -------------Definir endereço padrão----------------
	 // 🎯 Regras
	 // ✅ Apenas o dono do endereço pode defini-lo como padrão.
	 // ✅ O id virá pela URL.
	 // ✅ O endereço deve existir.
	 // ✅ Todos os demais endereços do usuário perderão o status de padrão.
	 // ✅ O endereço informado será definido como padrão.
	 // ✅ updatedAt será atualizado automaticamente.
	 // ✅ Registrar auditoria (SET_DEFAULT_ADDRESS).
	 // ✅ Retornar 400 caso o endereço não exista.
	 // ==================================================================================
    public AddressResponse setDefault(String id) {

        User user = authenticatedUserService.getCurrentUser();

        Address address = findAddress(id, user);

        List<Address> addresses = addressRepository.findByUser(user);

        for (Address item : addresses) {

            item.setDefaultAddress(false);
            item.setUpdatedAt(LocalDateTime.now());

        }

        address.setDefaultAddress(true);
        address.setUpdatedAt(LocalDateTime.now());

        addressRepository.saveAll(addresses);

        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "SET_DEFAULT_ADDRESS"
        );

        return AddressMapper.toResponse(address);
    }
}
