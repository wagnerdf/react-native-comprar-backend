# Módulo Freight Calculation

## Objetivo

Responsável pelo cálculo e apresentação das opções de frete disponíveis para uma determinada solicitação.

O módulo utiliza as informações cadastradas em **Carrier** e **ShippingOption** para localizar um serviço de entrega válido e retornar seus dados de preço e prazo estimado.

A primeira versão do módulo possui como objetivo estruturar a infraestrutura do cálculo de frete, permitindo sua evolução futura para regras mais completas de cálculo.

---

# Segurança

O cálculo de frete é uma funcionalidade destinada ao fluxo de compra da plataforma.

Na primeira versão, o endpoint poderá ser utilizado para consulta das opções de frete disponíveis.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Calcular Frete | ✅ | ✅ | ✅ |

> **Observação:** A regra definitiva de autorização poderá ser ajustada conforme a integração do cálculo de frete com o módulo Order e o fluxo de checkout.

---

# Conceito

O cálculo de frete utiliza uma **ShippingOption** previamente cadastrada.

Uma ShippingOption está relacionada a uma Carrier e contém:

- nome do serviço;
- preço;
- prazo estimado;
- status de ativação.

Fluxo básico:

```text
FreightCalculationRequest
        ↓
ShippingOption
        ↓
Carrier
        ↓
Preço
        ↓
Prazo estimado
        ↓
FreightCalculationResponse
```

---

# Entidades relacionadas

## Carrier

Transportadora responsável pelo serviço de entrega.

Exemplos:

- Correios
- Total Express
- Jadlog
- Loggi
- J&T Express

A Carrier deve estar ativa para que seus serviços possam ser utilizados.

---

## ShippingOption

Representa uma modalidade de entrega disponibilizada por uma transportadora.

Campos utilizados no cálculo:

- id
- carrier
- serviceName
- price
- estimatedDays
- active

Exemplos de serviços:

```text
Carrier: Correios
Service: PAC

Carrier: Correios
Service: SEDEX

Carrier: Total Express
Service: Expresso
```

---

# Request

## FreightCalculationRequest

A primeira versão do cálculo utiliza a identificação da ShippingOption.

Campos:

- shippingOptionId

Exemplo:

```json
{
    "shippingOptionId": "7583c7eb-c4f2-427b-bad7-cf81932b22a8"
}
```

---

# Response

## FreightCalculationResponse

A primeira versão retorna:

- shippingOptionId
- carrierName
- serviceName
- price
- estimatedDays

Exemplo:

```json
{
    "shippingOptionId": "7583c7eb-c4f2-427b-bad7-cf81932b22a8",
    "carrierName": "Total Express",
    "serviceName": "PAC",
    "price": 28.90,
    "estimatedDays": 7
}
```

---

# Regras de Negócio

## Localização da ShippingOption

- A ShippingOption informada deve existir.
- Caso não exista, retornar erro `404 NOT FOUND`.
- A ShippingOption deve estar ativa.
- ShippingOption inativa não pode ser utilizada no cálculo.
- A Carrier relacionada deve existir.
- A Carrier relacionada deve estar ativa.

---

## Preço

Na primeira versão:

- o preço é obtido diretamente da ShippingOption;
- não existe cálculo baseado em peso;
- não existe cálculo baseado em dimensões;
- não existe cálculo baseado em distância;
- não existe cálculo baseado em faixa de CEP;
- não existe cálculo baseado no valor do pedido.

O valor retornado representa o preço base cadastrado para o serviço.

---

## Prazo

Na primeira versão:

- o prazo é obtido diretamente da ShippingOption;
- o campo `estimatedDays` representa a quantidade estimada de dias para entrega;
- não existe cálculo dinâmico de prazo;
- não existe integração com APIs externas.

---

# Serviço

## FreightService

Responsável pela execução das regras de cálculo de frete.

Fluxo atual:

```text
calculate(request)
        ↓
Localiza ShippingOption
        ↓
Valida existência
        ↓
Valida ShippingOption ativa
        ↓
Obtém Carrier
        ↓
Obtém nome da Carrier
        ↓
Obtém nome do serviço
        ↓
Obtém preço
        ↓
Obtém prazo estimado
        ↓
Monta FreightCalculationResponse
        ↓
Retorna resposta
```

---

# Controller

## FreightController

Responsável por disponibilizar o cálculo de frete através da API REST.

Endpoint inicial:

```http
POST /freight/calculate
```

Request:

```json
{
    "shippingOptionId": "7583c7eb-c4f2-427b-bad7-cf81932b22a8"
}
```

Response:

```json
{
    "shippingOptionId": "7583c7eb-c4f2-427b-bad7-cf81932b22a8",
    "carrierName": "Total Express",
    "serviceName": "PAC",
    "price": 28.90,
    "estimatedDays": 7
}
```

**Status:** ✅ Implementado

---

# Endpoints

## Calcular Frete

```http
POST /freight/calculate
```

**Status:** ✅ Implementado

Responsável por localizar uma ShippingOption ativa e retornar seus dados de preço e prazo.

---

# Validações

## ShippingOption inexistente

Caso o ID informado não exista:

```text
HTTP 404 - NOT FOUND
```

Mensagem esperada:

```text
Shipping option not found: {id}
```

---

## ShippingOption inativa

Caso a opção de frete esteja desativada:

```text
HTTP 400 - BAD REQUEST
```

Mensagem esperada:

```text
Shipping option is inactive.
```

---

## Carrier inativa

Caso a transportadora relacionada esteja inativa:

```text
HTTP 400 - BAD REQUEST
```

Mensagem esperada:

```text
Carrier is inactive.
```

> A regra poderá ser refinada posteriormente conforme a evolução da integração entre Carrier, ShippingOption e Freight.

---

# Auditoria

Na primeira versão, o cálculo de frete ainda não possui auditoria obrigatória.

Futuramente poderá ser registrado:

```text
CALCULATE_FREIGHT
```

Caso seja necessário rastrear consultas de frete realizadas pelos usuários.

---

# Integrações

## Carrier

Responsável por:

- identificar a transportadora;
- validar se está ativa;
- fornecer o nome da transportadora.

---

## ShippingOption

Responsável por:

- identificar o serviço de entrega;
- fornecer o preço base;
- fornecer o prazo estimado;
- informar se o serviço está ativo.

---

## Order

Integração futura.

O módulo Order deverá utilizar o cálculo de frete durante o processo de compra.

Fluxo previsto:

```text
Order
        ↓
FreightService
        ↓
ShippingOption
        ↓
Carrier
        ↓
Preço do Frete
        ↓
Order Total
```

---

# Cálculo futuro

A primeira versão utiliza apenas o preço cadastrado na ShippingOption.

O cálculo deverá evoluir para considerar fatores adicionais.

## CEP de origem

Possibilitar cálculo baseado no local de origem da entrega.

---

## CEP de destino

Possibilitar cálculo baseado no endereço de destino.

---

## Peso

O preço poderá variar conforme o peso total dos produtos.

---

## Dimensões

O cálculo poderá considerar:

- altura;
- largura;
- comprimento;
- peso cúbico.

---

## Distância

O cálculo poderá considerar a distância entre origem e destino.

---

## Valor do pedido

Poderão existir regras relacionadas ao valor total da compra.

Exemplo:

```text
Pedido acima de R$ 199,00
        ↓
Frete grátis
```

---

## Frete grátis

Possibilidade de criar regras de frete grátis.

Exemplos:

- valor mínimo do pedido;
- região específica;
- campanha promocional;
- cliente específico;
- cupom.

---

# Integrações externas futuras

O módulo poderá futuramente integrar APIs de transportadoras e plataformas de logística.

Possíveis integrações:

| Status | Integração |
|:------:|------------|
| ⏳ | Correios |
| ⏳ | Jadlog |
| ⏳ | J&T Express |
| ⏳ | Loggi |
| ⏳ | Melhor Envio |
| ⏳ | Frenet |

---

# Arquitetura

```text
Controller
        ↓
FreightService
        ↓
ShippingOptionRepository
        ↓
ShippingOption
        ↓
Carrier
```

Os mapeamentos entre Entity e DTO são realizados conforme a estrutura de DTOs do módulo.

---

# Fluxo Implementado

## Cálculo atual

```text
POST /freight/calculate
        ↓
Recebe shippingOptionId
        ↓
Localiza ShippingOption
        ↓
ShippingOption existe?
        ↓
Valida active
        ↓
Obtém Carrier
        ↓
Obtém carrierName
        ↓
Obtém serviceName
        ↓
Obtém price
        ↓
Obtém estimatedDays
        ↓
Monta Response
        ↓
HTTP 200
```

---

# Testes

## Cadastro da infraestrutura

O cálculo utiliza ShippingOptions previamente cadastradas.

---

## Cálculo válido

Request:

```json
{
    "shippingOptionId": "7583c7eb-c4f2-427b-bad7-cf81932b22a8"
}
```

Resultado esperado:

```text
HTTP 200 - OK
```

---

## ShippingOption inexistente

Resultado esperado:

```text
HTTP 404 - NOT FOUND
```

---

## ShippingOption inativa

Resultado esperado:

```text
HTTP 400 - BAD REQUEST
```

---

## Carrier inativa

Resultado esperado:

```text
HTTP 400 - BAD REQUEST
```

---

# Roadmap do Módulo

## Funcionalidades básicas

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | FreightService |
| ✅ | FreightController |
| ✅ | Request de cálculo |
| ✅ | Response de cálculo |
| ✅ | Localização da ShippingOption |
| ✅ | Validação da ShippingOption |
| ✅ | Retorno do preço |
| ✅ | Retorno do prazo |

---

## Regras de cálculo

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | CEP de origem |
| ⏳ | CEP de destino |
| ⏳ | Peso |
| ⏳ | Dimensões |
| ⏳ | Peso cúbico |
| ⏳ | Distância |
| ⏳ | Faixas de CEP |
| ⏳ | Valor mínimo do pedido |
| ⏳ | Frete grátis |

---

## Integração com Order

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Seleção da ShippingOption no pedido |
| ⏳ | Inclusão do frete no total do pedido |
| ⏳ | Persistência do valor do frete |
| ⏳ | Persistência do prazo estimado |
| ⏳ | Snapshot da opção de frete utilizada |
| ⏳ | Recalcular frete durante alteração do pedido |

---

## Integrações externas

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | API Correios |
| ⏳ | API Jadlog |
| ⏳ | API J&T Express |
| ⏳ | API Loggi |
| ⏳ | Melhor Envio |
| ⏳ | Frenet |

---

## Auditoria

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Auditoria de cálculo de frete |
| ⏳ | Histórico de valores calculados |
| ⏳ | Histórico da ShippingOption utilizada |

---

# Próximas Etapas

A evolução recomendada do módulo é:

```text
Freight básico
        ↓
Validações
        ↓
Testes
        ↓
CEP origem/destino
        ↓
Peso
        ↓
Dimensões
        ↓
Regras de preço
        ↓
Frete grátis
        ↓
Integração com APIs externas
        ↓
Integração com Order
```

O módulo Freight Calculation será responsável pela infraestrutura de cálculo de frete da plataforma, utilizando Carrier e ShippingOption como base para as regras de entrega.
