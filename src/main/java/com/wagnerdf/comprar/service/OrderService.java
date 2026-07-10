package com.wagnerdf.comprar.service;

import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.CreateOrderItemRequest;
import com.wagnerdf.comprar.dto.request.CreateOrderRequest;
import com.wagnerdf.comprar.entity.Product;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.ProductNotFoundException;
import com.wagnerdf.comprar.repository.OrderRepository;
import com.wagnerdf.comprar.repository.ProductRepository;
import com.wagnerdf.comprar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
    
    public void createOrder(CreateOrderRequest request) {
    	
    	 for (CreateOrderItemRequest itemRequest : request.getItems()) {

    	        Product product = validateProduct(itemRequest);

    	        // criar o orderItem e calcular subtotal
   	
    	
    	}

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

}
