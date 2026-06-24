package com.wagnerdf.comprar.dto.response;

import com.wagnerdf.comprar.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponse {

    private String id;
    private String name;
    private String email;
    private Gender gender;
}
