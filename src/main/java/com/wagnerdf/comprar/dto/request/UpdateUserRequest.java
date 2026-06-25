package com.wagnerdf.comprar.dto.request;

import com.wagnerdf.comprar.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate birthDate;

    @NotNull(message = "Gender é obrigatório")
    private Gender gender;
}
