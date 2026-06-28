package com.wagnerdf.comprar.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wagnerdf.comprar.entity.Auth;
import com.wagnerdf.comprar.entity.User;
import com.wagnerdf.comprar.exception.AuthenticationException;
import com.wagnerdf.comprar.repository.AuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final AuthRepository authRepository;

    public String getCurrentUsername() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    public User getCurrentUser() {

        String username = getCurrentUsername();

        Auth auth = authRepository.findByUsername(username)
                .orElseThrow(() ->
                        new AuthenticationException("Usuário não autenticado."));

        if (!auth.getUser().getActive()) {
            throw new AuthenticationException("Usuário inativo.");
        }

        return auth.getUser();
    }
    
    
}
