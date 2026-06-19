package com.wagnerdf.comprar.dto.request;

import com.wagnerdf.comprar.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private LocalDate birthDate;
    private Gender gender;

    private String username;
    private String password;
}
