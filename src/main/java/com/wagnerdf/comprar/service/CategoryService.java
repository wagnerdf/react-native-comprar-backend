package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.CategoryRequest;
import com.wagnerdf.comprar.dto.response.CategoryResponse;
import com.wagnerdf.comprar.entity.Category;
import com.wagnerdf.comprar.exception.BusinessException;
import com.wagnerdf.comprar.exception.CategoryNotFoundException;
import com.wagnerdf.comprar.mapper.CategoryMapper;
import com.wagnerdf.comprar.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuditService auditService;
    private final AuthenticatedUserService authenticatedUserService;

    // ================================================================================
    // ----------------Cadastro de Categoria----------------
    // 🎯 Regras
    // ✅ Apenas ADMIN poderá cadastrar categorias.
    // ✅ Nome obrigatório.
    // ✅ Nome único.
    // ✅ Categoria nasce ativa.
    // ✅ createdAt recebe data atual.
    // ✅ updatedAt recebe data atual.
    // ✅ Registrar auditoria (CREATE_CATEGORY).
    // ================================================================================

    private String normalizeCategoryName(String name) {
        return name.trim();
    }
    
    public CategoryResponse create(CategoryRequest request) {
    	
    	String categoryName = normalizeCategoryName(request.getName());

        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new BusinessException("Categoria já cadastrada.");
        }

        Category category = CategoryMapper.toEntity(request);

        category.setName(categoryName);
        category.setActive(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category saved = categoryRepository.save(category);

        auditService.log(
                authenticatedUserService.getCurrentUser().getEmail(),
                "CREATE_CATEGORY"
        );

        return CategoryMapper.toResponse(saved);
    }
    
	 // ================================================================================
	 // ----------------Listagem de Categorias----------------
	 // 🎯 Regras
	 // ✅ Listar somente categorias ativas.
	 // ✅ Retornar paginação.
	 // ✅ Ordenar por nome.
	 // ✅ Não registrar auditoria.
	 // ================================================================================
	
	 public Page<CategoryResponse> list(Pageable pageable) {
	
	     return categoryRepository
	             .findByActiveTrue(pageable)
	             .map(CategoryMapper::toResponse);
	
	 }
	 
	// ================================================================================
	// ----------------Edição de Categoria----------------
	// 🎯 Regras
	// ✅ Apenas ADMIN poderá editar categorias.
	// ✅ Categoria deve existir.
	// ✅ Nome continua único.
	// ✅ Não alterar id.
	// ✅ Não alterar active.
	// ✅ Não alterar createdAt.
	// ✅ Atualizar updatedAt.
	// ✅ Registrar auditoria (UPDATE_CATEGORY).
	// ================================================================================

	public CategoryResponse update(String id, CategoryRequest request) {

	    Category category = findCategory(id);
	    
	    String categoryName = normalizeCategoryName(request.getName());

	    categoryRepository.findByNameIgnoreCase(categoryName)
	            .ifPresent(existing -> {

	                if (!existing.getId().equals(category.getId())) {
	                    throw new BusinessException("Categoria já cadastrada.");
	                }

	            });

	    category.setName(categoryName);
	    category.setDescription(request.getDescription());

	    category.setUpdatedAt(LocalDateTime.now());

	    Category updated = categoryRepository.save(category);

	    auditService.log(
	            authenticatedUserService.getCurrentUser().getEmail(),
	            "UPDATE_CATEGORY"
	    );

	    return CategoryMapper.toResponse(updated);

	}
	
	// ================================================================================
	// ----------------Busca de Categoria----------------
	// 🎯 Regras
	// ✅ Centraliza a busca por categoria.
	// ✅ Retorna 404 caso não exista.
	// ================================================================================

	private Category findCategory(String id) {

	    return categoryRepository
	            .findByIdAndActiveTrue(id)
	            .orElseThrow(() ->
	                    new CategoryNotFoundException(
	                            "Categoria não encontrada."
	                    ));

	}

}
