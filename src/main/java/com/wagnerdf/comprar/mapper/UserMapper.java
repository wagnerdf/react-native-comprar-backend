package com.wagnerdf.comprar.mapper;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.entity.User;

public class UserMapper {

    private UserMapper() {
        // evita instanciar (classe utilitária)
    }

    public static User toEntity(RegisterRequest request) {

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .build();
    }
}
