package com.wagnerdf.comprar.dto.response;

import java.time.LocalDate;

import com.wagnerdf.comprar.enums.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {

    private String id;

    private String name;

    private String email;

    private LocalDate birthDate;

    private Gender gender;

    private Boolean active;

    private String username;

}