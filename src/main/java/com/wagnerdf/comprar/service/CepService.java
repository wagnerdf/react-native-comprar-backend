package com.wagnerdf.comprar.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wagnerdf.comprar.dto.response.CepResponse;
import com.wagnerdf.comprar.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CepService {

    private final RestTemplate restTemplate;

    // ================================================================================
    // ----------------Consulta de CEP----------------
    // 🎯 Regras
    // ✅ Remove caracteres não numéricos do CEP.
    // ✅ CEP deve possuir 8 dígitos.
    // ✅ Consulta o ViaCEP.
    // ✅ CEP deve existir.
    // ✅ Retorna os dados do CEP consultado.
    // ================================================================================

    public CepResponse findByCep(String cep) {

        String normalizedCep = cep.replaceAll("\\D", "");

        if (!normalizedCep.matches("\\d{8}")) {
            throw new BusinessException(
                    "CEP must contain exactly 8 digits.");
        }

        String url =
                "https://viacep.com.br/ws/"
                + normalizedCep
                + "/json/";

        CepResponse response =
                restTemplate.getForObject(
                        url,
                        CepResponse.class
                );

        if (response == null) {
            throw new BusinessException(
                    "Unable to consult ZIP code.");
        }

        if (Boolean.TRUE.equals(response.erro())) {
            throw new BusinessException(
                    "ZIP code not found: " + normalizedCep);
        }

        return response;
    }
}