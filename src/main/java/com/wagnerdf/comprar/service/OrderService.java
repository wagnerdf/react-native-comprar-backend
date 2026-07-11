package com.wagnerdf.comprar.service;

import java.math.BigDecimal;
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
import com.wagnerdf.comprar.dto.response.OrderListResponse;
import com.wagnerdf.comprar.dto.response.OrderResponse;
import com.wagnerdf.comprar.entity.Order;
import com.wagnerdf.comprar.entity.OrderItem;
import com.wagnerdf.comprar.entity.Product;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.OrderStatus;
import com.wagnerdf.comprar.exception.BusinessException;
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

}
