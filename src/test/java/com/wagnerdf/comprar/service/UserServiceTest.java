package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@SpringBootTest
@ActiveProfiles("test") 
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {

        User user = User.builder()
                .name("Wagner Test")
                .email("wagner.test@email.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .build();

        userService.createUser(user, "wagner_test", "123456");

        System.out.println("Teste executado com sucesso!");
    }
}
