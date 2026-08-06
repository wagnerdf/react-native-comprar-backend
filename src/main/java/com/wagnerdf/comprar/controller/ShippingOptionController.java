package com.wagnerdf.comprar.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.ShippingOptionRequest;
import com.wagnerdf.comprar.dto.request.ShippingOptionUpdateRequest;
import com.wagnerdf.comprar.dto.response.ShippingOptionListResponse;
import com.wagnerdf.comprar.dto.response.ShippingOptionResponse;
import com.wagnerdf.comprar.dto.response.SuccessResponse;
import com.wagnerdf.comprar.service.ShippingOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * ================================================================================
 * Controller - Shipping Option
 *
 * Responsável pelo gerenciamento das opções de envio vinculadas às transportadoras.
 *
 * Permite:
 *
 * ✅ Cadastro de opções de frete.
 * ✅ Consulta de opções disponíveis.
 * ✅ Atualização de serviços.
 * ✅ Soft Delete.
 * ✅ Reativação.
 *
 * As opções cadastradas serão utilizadas futuramente
 * no cálculo de frete dos pedidos.
 *
 * ================================================================================
 */
@RestController
@RequestMapping("/shipping-options")
@RequiredArgsConstructor
public class ShippingOptionController {
	
	private final ShippingOptionService shippingOptionService;
	
	// =============================================================================
	// ----------------Cadastro de Opção de Frete----------------
	//
	// 🎯 Regras
	// ✅ Apenas usuários com CREATE_SHIPPING_OPTION.
	// ✅ Carrier deve existir.
	// ✅ Carrier deve estar ativo.
	// ✅ Nome do serviço obrigatório.
	// ✅ Remove espaços no início e fim do nome.
	// ✅ Não permite duplicidade para a mesma transportadora.
	// ✅ Opção nasce ativa.
	// ✅ createdAt recebe data atual.
	// ✅ updatedAt recebe data atual.
	// ✅ Registrar auditoria (CREATE_SHIPPING_OPTION).
	//
	// =============================================================================
	@PostMapping
	@PreAuthorize("hasAuthority('CREATE_SHIPPING_OPTION')")
	public ResponseEntity<ShippingOptionResponse> create(

	        @Valid
	        @RequestBody
	        ShippingOptionRequest request) {

	    return ResponseEntity
	            .status(HttpStatus.CREATED)
	            .body(shippingOptionService.create(request));

	}
	
	// =============================================================================
	// ----------------Consulta de Opção de Frete por ID----------------
	//
	// 🎯 Regras
	// ✅ Busca opção pelo identificador informado.
	// ✅ Retorna apenas registros existentes.
	// ✅ Caso não exista lança ShippingOptionNotFoundException.
	//
	// =============================================================================
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_SHIPPING_OPTION')")
	public ResponseEntity<ShippingOptionResponse> findById(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.findById(id));

	}
	
	// =============================================================================
	// ----------------Listagem de Opções de Frete----------------
	//
	// 🎯 Regras
	// ✅ Retorna opções cadastradas.
	// ✅ Utiliza paginação.
	// ✅ Permite ordenação via Pageable.
	// ✅ Usado futuramente pelo cálculo de frete.
	//
	// =============================================================================
	@GetMapping
	@PreAuthorize("hasAuthority('READ_SHIPPING_OPTION')")
	public ResponseEntity<Page<ShippingOptionListResponse>> findAll(
	        Pageable pageable) {

	    return ResponseEntity.ok(
	            shippingOptionService.findAll(pageable));

	}
	
	// =============================================================================
	// ----------------Atualização de Opção de Frete----------------
	//
	// 🎯 Regras
	// ✅ Apenas usuários com UPDATE_SHIPPING_OPTION.
	// ✅ Opção deve existir.
	// ✅ Permite alterar nome do serviço.
	// ✅ Permite alterar preço.
	// ✅ Permite alterar prazo estimado.
	// ✅ Remove espaços extras do nome.
	// ✅ Não permite duplicidade para mesma transportadora.
	// ✅ Atualiza updatedAt.
	// ✅ Registrar auditoria (UPDATE_SHIPPING_OPTION).
	//
	// =============================================================================
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('UPDATE_SHIPPING_OPTION')")
	public ResponseEntity<ShippingOptionResponse> update(

	        @PathVariable
	        String id,

	        @Valid
	        @RequestBody
	        ShippingOptionUpdateRequest request) {

	    return ResponseEntity.ok(
	            shippingOptionService.update(id, request));

	}
	
	// =============================================================================
	// ----------------Exclusão de Opção de Frete----------------
	//
	// 🎯 Regras
	// ✅ Apenas usuários com DELETE_SHIPPING_OPTION.
	// ✅ Não remove fisicamente o registro.
	// ✅ Executa Soft Delete.
	// ✅ Altera active para false.
	// ✅ Atualiza updatedAt.
	// ✅ Não permite excluir registro já inativo.
	// ✅ Registrar auditoria (DELETE_SHIPPING_OPTION).
	//
	// =============================================================================
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_SHIPPING_OPTION')")
	public ResponseEntity<SuccessResponse> delete(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.delete(id));

	}
	
	// =============================================================================
	// ----------------Reativação de Opção de Frete----------------
	//
	// 🎯 Regras
	// ✅ Apenas usuários com REACTIVATE_SHIPPING_OPTION.
	// ✅ Opção deve existir.
	// ✅ Deve estar desativada.
	// ✅ Altera active para true.
	// ✅ Atualiza updatedAt.
	// ✅ Permite reutilização no cálculo de frete.
	// ✅ Registrar auditoria (REACTIVATE_SHIPPING_OPTION).
	//
	// =============================================================================
	@PatchMapping("/{id}/reactivate")
	@PreAuthorize("hasAuthority('REACTIVATE_SHIPPING_OPTION')")
	public ShippingOptionResponse reactivate(
	        @PathVariable String id) {

	    return shippingOptionService.reactivate(id);

	}

}
