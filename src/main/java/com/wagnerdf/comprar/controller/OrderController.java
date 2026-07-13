package com.wagnerdf.comprar.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.comprar.dto.request.CreateOrderRequest;
import com.wagnerdf.comprar.dto.request.UpdateOrderStatusRequest;
import com.wagnerdf.comprar.dto.response.OrderDetailResponse;
import com.wagnerdf.comprar.dto.response.OrderListResponse;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * ==========================================================
     * LISTAGEM DE PEDIDOS
     * ==========================================================
     *
     * Endereçamentos suportados:
     *
     * GET /orders
     *      Lista pedidos de forma paginada.
     *
     * GET /orders?page=0&size=10
     *      Paginação personalizada.
     *
     * GET /orders?status=PENDING
     *      (Implementação futura)
     *      Filtrar pedidos por status.
     *
     * GET /orders?orderNumber=ORD-1001
     *      (Implementação futura)
     *      Buscar por número do pedido.
     *
     * GET /orders?startDate=2026-07-01&endDate=2026-07-31
     *      (Implementação futura)
     *      Buscar pedidos por período.
     *
     * GET /orders?customer=Wagner
     *      (Implementação futura)
     *      Buscar pedidos por cliente.
     *
     * GET /orders?sort=createdAt,desc
     *      (Implementação futura)
     *      Ordenação dinâmica.
     *
     * ==========================================================
     * Regras de acesso
     * ==========================================================
     *
     * USER
     *      Visualiza apenas seus próprios pedidos.
     *
     * EMPLOYEE
     *      Visualiza todos os pedidos.
     *
     * ADMIN
     *      Visualiza todos os pedidos.
     *
     * ==========================================================
     * Ordenação atual
     * ==========================================================
     *
     * createdAt DESC
     *
     * ==========================================================
     * Evoluções futuras
     * ==========================================================
     *
     * - filtros combinados;
     * - paginação dinâmica;
     * - Specification;
     * - busca textual;
     * - exportação CSV;
     * - exportação PDF.
     *
     */
    @GetMapping
    @PreAuthorize("hasAuthority('READ_ORDER')")
    public Page<OrderListResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return orderService.getOrders(page, size);

    }
    
    /**
     * ==========================================================
     * CONSULTAR PEDIDO POR ID
     * ==========================================================
     *
     * Localiza um pedido pelo seu identificador.
     *
     * Regras de acesso:
     *
     * ADMIN
     *      Pode consultar qualquer pedido.
     *
     * EMPLOYEE
     *      Pode consultar qualquer pedido.
     *
     * USER
     *      Pode consultar apenas pedidos de sua propriedade.
     *
     * Exceções:
     *
     * OrderNotFoundException
     *      Quando o pedido não existir.
     *
     * ForbiddenException
     *      Quando um usuário tentar acessar pedido de outro usuário.
     *
     * Fluxo:
     *
     * Buscar pedido
     *      ↓
     * Pedido existe?
     *      ↓
     * Validar permissões
     *      ↓
     * Converter para DTO
     *      ↓
     * Retornar resposta
     *
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ORDER')")
    public OrderDetailResponse getOrderById(
            @PathVariable String id
    ) {

        return orderService.getOrderById(id);

    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<Void> updateOrderStatus(

            @PathVariable String id,

            @Valid
            @RequestBody UpdateOrderStatusRequest request

    ) {

        orderService.updateOrderStatus(id, request);

        return ResponseEntity.noContent().build();

    }
    
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('READ_ORDER')")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable String id
    ) {

        orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();

    }

}
