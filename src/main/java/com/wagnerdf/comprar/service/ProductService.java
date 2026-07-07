package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.comprar.dto.request.CreateProductRequest;
import com.wagnerdf.comprar.dto.response.ProductDetailResponse;
import com.wagnerdf.comprar.dto.response.ProductListResponse;
import com.wagnerdf.comprar.entity.Category;
import com.wagnerdf.comprar.entity.Product;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.CategoryNotFoundException;
import com.wagnerdf.comprar.exception.ProductNotFoundException;
import com.wagnerdf.comprar.mapper.ProductMapper;
import com.wagnerdf.comprar.repository.CategoryRepository;
import com.wagnerdf.comprar.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AuditService auditService;
    private final AuthenticatedUserService authenticatedUserService;
    
    @Transactional
    public void createProduct(CreateProductRequest request) {

        // =========================
        // VALIDAÇÕES
        // =========================
        if (productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("SKU já cadastrado");
        }

        // =========================
        // CATEGORY
        // =========================
        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Categoria não encontrada"));

        // =========================
        // MAPPER
        // =========================
        Product product = ProductMapper.toEntity(request);

        // =========================
        // REGRAS DE NEGÓCIO
        // =========================
        product.setCategory(category);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // =========================
        // SAVE
        // =========================
        productRepository.save(product);

        // =========================
        // AUDITORIA
        // =========================
        auditService.log(
                authenticatedUserService.getCurrentUsername(),
                "CREATE_PRODUCT"
        );

    }
    
    @Transactional(readOnly = true)
    public Page<ProductListResponse> findAll(
            Pageable pageable,
            String name
    ) {

        Page<Product> page;

        if (name != null && !name.isBlank()) {

            page = productRepository.findByActiveTrueAndNameContainingIgnoreCase(
                    name,
                    pageable
            );

        } else {

            page = productRepository.findByActiveTrue(pageable);

        }

        return page.map(product ->

                ProductListResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .sku(product.getSku())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .active(product.getActive())
                        .category(product.getCategory().getName())
                        .build()

        );

    }
    
    @Transactional(readOnly = true)
    public ProductDetailResponse findById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado."));

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .stock(product.getStock())
                .minimumStock(product.getMinimumStock())
                .active(product.getActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

    }

}
