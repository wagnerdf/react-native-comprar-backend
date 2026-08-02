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
 * Controller responsável pelo gerenciamento das opções de envio
 * disponibilizadas por cada transportadora.
 *
 * <p>Este módulo permite cadastrar, consultar, atualizar,
 * desativar e reativar serviços de entrega utilizados
 * futuramente no cálculo de frete dos pedidos.</p>
 */
@RestController
@RequestMapping("/shipping-options")
@RequiredArgsConstructor
public class ShippingOptionController {
	
	private final ShippingOptionService shippingOptionService;
	
	/**
	 * Cadastra uma nova opção de envio para uma transportadora.
	 *
	 * @param request dados da opção de envio
	 * @return opção de envio criada
	 */
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
	
	/**
	 * Busca uma opção de envio pelo identificador.
	 *
	 * @param id identificador da opção de envio
	 * @return opção de envio encontrada
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ShippingOptionResponse> findById(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.findById(id));

	}
	
	/**
	 * Lista as opções de envio de forma paginada.
	 *
	 * @param pageable informações de paginação e ordenação
	 * @return página contendo as opções de envio
	 */
	@GetMapping
	public ResponseEntity<Page<ShippingOptionListResponse>> findAll(
	        Pageable pageable) {

	    return ResponseEntity.ok(
	            shippingOptionService.findAll(pageable));

	}
	
	/**
	 * Atualiza uma opção de envio existente.
	 *
	 * @param id identificador da opção de envio
	 * @param request novos dados da opção de envio
	 * @return opção de envio atualizada
	 */
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
	
	/**
	 * Realiza a exclusão lógica (Soft Delete) de uma opção de envio.
	 *
	 * @param id identificador da opção de envio
	 * @return mensagem de sucesso
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('DELETE_SHIPPING_OPTION')")
	public ResponseEntity<SuccessResponse> delete(
	        @PathVariable String id) {

	    return ResponseEntity.ok(
	            shippingOptionService.delete(id));

	}
	
	/**
	 * Reativa uma opção de envio previamente desativada.
	 *
	 * @param id identificador da opção de envio
	 * @return opção de envio reativada
	 */
	@PatchMapping("/{id}/reactivate")
	@PreAuthorize("hasAuthority('REACTIVATE_SHIPPING_OPTION')")
	public ShippingOptionResponse reactivate(
	        @PathVariable String id) {

	    return shippingOptionService.reactivate(id);

	}

}
