package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.request.CategoryRequest;
import com.wagnerdf.comprar.dto.response.CategoryResponse;
import com.wagnerdf.comprar.entity.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryRequest request) {

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

    }

    public static CategoryResponse toResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.getActive())
                .build();

    }

}