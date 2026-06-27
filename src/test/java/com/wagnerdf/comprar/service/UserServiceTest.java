package com.wagnerdf.comprar.service;

import com.wagnerdf.comprar.dto.request.RegisterRequest;
import com.wagnerdf.comprar.enums.Gender;
import com.wagnerdf.comprar.repository.AuthRepository;
import com.wagnerdf.comprar.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test") 
class UserServiceTest {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthRepository authRepository;

    @Test
    void shouldCreateUserSuccessfully() {

    	RegisterRequest request = new RegisterRequest();

        request.setName("Wagner Test");
        request.setEmail("wagner.test@email.com");
        request.setUsername("wagner_test");
        request.setPassword("123456");
        request.setBirthDate(LocalDate.of(1990, 1, 1));
        request.setGender(Gender.MALE);

        userService.createUser(request);

        boolean userEmailExists =
                userRepository.findByEmail("wagner.test@email.com").isPresent();
        assertTrue(userEmailExists);
        
        boolean userNameExists =
        		authRepository.findByUsername("wagner_test").isPresent();
        assertTrue(userNameExists);
    }
}
