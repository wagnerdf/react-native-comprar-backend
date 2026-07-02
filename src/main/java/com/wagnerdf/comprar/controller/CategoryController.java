package com.wagnerdf.comprar.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.comprar.dto.request.CategoryRequest;
import com.wagnerdf.comprar.dto.response.CategoryResponse;
import com.wagnerdf.comprar.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Page<CategoryResponse>> list(

            @PageableDefault(
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                categoryService.list(pageable)
        );

    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(

            @PathVariable String id,

            @Valid
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.update(id, request)
        );

    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();

    }
    
    @PatchMapping("/{id}/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> reactivate(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                categoryService.reactivate(id)
        );

    }

}
