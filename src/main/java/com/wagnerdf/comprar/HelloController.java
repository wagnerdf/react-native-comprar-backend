package com.wagnerdf.comprar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        System.out.println("Backend rodando OK 🚀");
        return "Hello, backend is running!";
    }
}