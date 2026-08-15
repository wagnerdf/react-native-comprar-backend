package com.wagnerdf.comprar.dto.response;

/**
 * DTO responsável por representar os dados retornados
 * pelo serviço de consulta de CEP.
 *
 * Utilizado pelo módulo de cálculo de frete para
 * identificar principalmente a UF de origem e destino.
 */
public record CepResponse(

        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf,
        String estado,
        String regiao,
        String ibge,
        Boolean erro

) {
}