package com.wagnerdf.comprar.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.dto.request.CategoryRequest;
import com.wagnerdf.comprar.dto.response.CategoryResponse;
import com.wagnerdf.comprar.entity.Category;
import com.wagnerdf.comprar.exception.BusinessException;
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

    public CategoryResponse create(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessException("Categoria já cadastrada.");
        }

        Category category = CategoryMapper.toEntity(request);

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

}
