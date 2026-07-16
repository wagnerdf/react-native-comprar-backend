package com.wagnerdf.comprar.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.CreateOrderItemRequest;
import com.wagnerdf.comprar.dto.request.CreateOrderRequest;
import com.wagnerdf.comprar.dto.request.UpdateOrderStatusRequest;
import com.wagnerdf.comprar.dto.response.OrderDetailResponse;
import com.wagnerdf.comprar.dto.response.OrderListResponse;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.entity.Order;
import com.wagnerdf.comprar.entity.OrderItem;
import com.wagnerdf.comprar.entity.Product;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.OrderStatus;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.ForbiddenException;
import com.wagnerdf.comprar.exception.OrderNotFoundException;
import com.wagnerdf.comprar.exception.ProductNotFoundException;
import com.wagnerdf.comprar.mapper.OrderMapper;
import com.wagnerdf.comprar.repository.OrderRepository;
import com.wagnerdf.comprar.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final AuthenticatedUserService authenticatedUserService;
	private final StockMovementService stockMovementService;
    
    public OrderResponse createOrder(CreateOrderRequest request) {
    	
    	Order order = Order.builder()
                .build();
    	
    	User user = authenticatedUserService.getCurrentUser();

    	order.setUser(user);

        List<OrderItem> items = new ArrayList<>();
    	
    	 for (CreateOrderItemRequest itemRequest : request.getItems()) {

    	        Product product = validateProduct(itemRequest);
    	        
    	        OrderItem item = buildOrderItem(
    	                order,
    	                product,
    	                itemRequest
    	        );

    	        items.add(item);

    	}
    	 
    	 order.setItems(items);
    	 
    	 BigDecimal total = calculateOrderTotal(items);

    	 order.setTotal(total);
    	 
    	 order.setStatus(OrderStatus.PENDING);
    	 
    	 order.setOrderNumber(generateOrderNumber());
    	 
    	 Order savedOrder = orderRepository.save(order);
    	 
    	 orderRepository.save(order);

    	 decreaseStock(order, items);

    	 return OrderMapper.toResponse(savedOrder);

    }
    
    private Product validateProduct(CreateOrderItemRequest itemRequest) {

    	// =========================
        // Localizar produto
        // =========================
        Product product = productRepository
                .findById(itemRequest.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado."));

        // =========================
        // Validar produto ativo
        // =========================
        if (!product.getActive()) {
            throw new BusinessException(
                    "Produto está inativo."
            );
        }

        // =========================
        // Validar estoque
        // =========================
        if (product.getStock() < itemRequest.getQuantity()) {
            throw new BusinessException(
                    "Estoque insuficiente para o produto: "
                            + product.getName()
            );
        }

        return product;
    }
    
    private OrderItem buildOrderItem(Order order,
		            Product product,
		            CreateOrderItemRequest itemRequest) {
		
		BigDecimal subtotal = product.getPrice()
		.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
		
		return OrderItem.builder()
		.order(order)
		.product(product)
		.quantity(itemRequest.getQuantity())
		.unitPrice(product.getPrice())
		.subtotal(subtotal)
		.build();
		
		}
    
    private BigDecimal calculateOrderTotal(List<OrderItem> items) {

        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
    
    private String generateOrderNumber() {

        return "ORD-" + System.currentTimeMillis();

    }
    
    public Page<OrderListResponse> getOrders(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmployee =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        Page<Order> orders;

        if (isAdmin || isEmployee) {

            orders = orderRepository.findAll(pageable);

        } else {

            User user = authenticatedUserService.getCurrentUser();

            orders = orderRepository.findAllByUserId(
                    user.getId(),
                    pageable
            );

        }

        return orders.map(OrderMapper::toListResponse);

    }
    
    public OrderDetailResponse getOrderById(String id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Pedido não encontrado."));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmployee = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        if (!isAdmin && !isEmployee) {

            User user = authenticatedUserService.getCurrentUser();

            if (!order.getUser().getId().equals(user.getId())) {

                throw new ForbiddenException(
                        "Você não possui permissão para visualizar este pedido."
                );

            }

        }

        return OrderMapper.toDetailResponse(order);

    }
    
    public void updateOrderStatus(
            String id,
            UpdateOrderStatusRequest request
    ) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Pedido não encontrado."));

        validateStatusTransition(
                order.getStatus(),
                request.getStatus()
        );

        order.setStatus(request.getStatus());
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

    }
    
    /**
     * ==========================================================
     * VALIDAR TRANSIÇÃO DE STATUS
     * ==========================================================
     *
     * Valida se a mudança entre o status atual
     * e o novo status é permitida.
     *
     * Fluxo permitido:
     *
     * PENDING → PROCESSING
     *
     * PROCESSING → PAID
     *
     * PAID → SHIPPED
     *
     * SHIPPED → DELIVERED
     *
     * Estados finais:
     *
     * DELIVERED
     *
     * CANCELLED
     *
     * Não permitem novas alterações.
     *
     * Lança BusinessException quando
     * a transição não for permitida.
     *
     */
    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {

        switch (currentStatus) {

            case PENDING -> {

                if (newStatus != OrderStatus.PROCESSING) {
                    throw new BusinessException(
                            "Transição de status inválida."
                    );
                }

            }

            case PROCESSING -> {

                if (newStatus != OrderStatus.PAID) {
                    throw new BusinessException(
                            "Transição de status inválida."
                    );
                }

            }

            case PAID -> {

                if (newStatus != OrderStatus.SHIPPED) {
                    throw new BusinessException(
                            "Transição de status inválida."
                    );
                }

            }

            case SHIPPED -> {

                if (newStatus != OrderStatus.DELIVERED) {
                    throw new BusinessException(
                            "Transição de status inválida."
                    );
                }

            }

            case DELIVERED, CANCELLED ->

                    throw new BusinessException(
                            "O pedido não pode mais ter seu status alterado."
                    );

        }

    }
    
    public void cancelOrder(String id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Pedido não encontrado."));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isEmployee = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYEE"));

        if (!isAdmin && !isEmployee) {

            User user = authenticatedUserService.getCurrentUser();

            if (!order.getUser().getId().equals(user.getId())) {

                throw new ForbiddenException(
                        "Você não possui permissão para cancelar este pedido."
                );

            }

        }

        if (order.getStatus() != OrderStatus.PENDING) {

            throw new BusinessException(
                    "Somente pedidos com status PENDING podem ser cancelados."
            );

        }

        order.setStatus(OrderStatus.CANCELLED);
        restoreStock(order, order.getItems());
        orderRepository.save(order);

        orderRepository.save(order);

    }
    
    private void decreaseStock(Order order, List<OrderItem> items) {

        List<Product> products = new ArrayList<>();
    	
    	for (OrderItem item : items) {

            Product product = item.getProduct();
            
            Integer previousStock = product.getStock();

            product.setStock(
            		previousStock - item.getQuantity()
            );

            stockMovementService.registerExit(
                    product,
                    item.getQuantity(),
                    previousStock,
                    order
            );

            products.add(product);
        }

        productRepository.saveAll(products);
    }
    
    private void restoreStock(Order order, List<OrderItem> items) {

        List<Product> products = new ArrayList<>();

        for (OrderItem item : items) {

            Product product = item.getProduct();
            
            Integer previousStock = product.getStock();

            product.setStock(
                    previousStock + item.getQuantity()
            );
            
            stockMovementService.registerEntry(
                    product,
                    item.getQuantity(),
                    previousStock,
                    order
            );

            products.add(product);
        }

        productRepository.saveAll(products);
    }

}
