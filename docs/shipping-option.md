# Módulo Shipping Option

## Objetivo

Responsável pelo gerenciamento dos serviços de entrega disponibilizados por cada transportadora.

Cada transportadora poderá possuir um ou mais serviços de frete, contendo preço base, prazo estimado e status de ativação.

O módulo será utilizado futuramente pelo cálculo de frete durante a criação dos pedidos.

---

# Segurança

O gerenciamento das opções de frete pertence ao Backoffice da aplicação.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Criar | ✅ | ✅ | ❌ |
| Listar | ✅ | ✅ | ❌ |
| Consultar | ✅ | ✅ | ❌ |
| Atualizar | ✅ | ✅ | ❌ |
| Desativar | ✅ | ✅ | ❌ |
| Reativar | ✅ | ✅ | ❌ |

- CREATE_SHIPPING_OPTION
- READ_SHIPPING_OPTION
- UPDATE_SHIPPING_OPTION
- DELETE_SHIPPING_OPTION
- REACTIVATE_SHIPPING_OPTION

---

# Entidade

## ShippingOption

Campos

- id
- carrier
- serviceName
- price
- estimatedDays
- active
- createdAt
- updatedAt

---

# Regras de Negócio

## Cadastro

- ✅ A transportadora deve existir.
- ✅ A transportadora deve estar ativa.
- ✅ Não permite dois serviços com o mesmo nome para a mesma transportadora.
- ✅ Remove espaços em branco no início e fim do nome do serviço.
- ✅ O serviço é criado ativo por padrão.
- ✅ Registra auditoria (`CREATE_SHIPPING_OPTION`).
- ✅ carrierId obrigatório.
- ✅ serviceName obrigatório.
- ✅ price obrigatório.
- ✅ estimatedDays obrigatório.

---

## Consulta

- ✅ Consulta por ID.
- ✅ Listagem paginada.
- ✅ Ordenação por qualquer coluna suportada.

---

## Atualização

- ✅ O serviço deve existir.
- ✅ Não permite nome duplicado para a mesma transportadora.
- ✅ Atualiza automaticamente o campo `updatedAt`.
- ✅ Registra auditoria (`UPDATE_SHIPPING_OPTION`).
- ✅ Permite alterar nome do serviço.
- ✅ Permite alterar preço.
- ✅ Permite alterar prazo estimado.

---

## Desativação

Permitida apenas para registros ativos.

Ao desativar:

- altera `active` para `false`;
- atualiza `updatedAt`;
- registra auditoria (`DELETE_SHIPPING_OPTION`).

---

## Reativação

Permitida apenas para registros inativos.

Ao reativar:

- altera `active` para `true`;
- atualiza `updatedAt`;
- registra auditoria (`REACTIVATE_SHIPPING_OPTION`).

---

---

# Exceptions

O módulo utiliza exceções específicas:

| Exception | HTTP |
|----------|------|
| ShippingOptionNotFoundException | 404 |
| CarrierNotFoundException | 404 |
| BusinessException | 400 |
| ValidationException | 400 |

---

# Endpoints

## Criar

```http
POST /shipping-options
```

**Status:** ✅ Implementado

---

## Listar

```http
GET /shipping-options
```

**Status:** ✅ Implementado

- Paginação.
- Ordenação.

---

## Buscar por ID

```http
GET /shipping-options/{id}
```

**Status:** ✅ Implementado

---

## Atualizar

```http
PUT /shipping-options/{id}
```

**Status:** ✅ Implementado

---

## Desativar

```http
DELETE /shipping-options/{id}
```

**Status:** ✅ Implementado

Soft Delete.

---

## Reativar

```http
PATCH /shipping-options/{id}/reactivate
```

**Status:** ✅ Implementado

---

# Auditoria

Operações registradas

| Status | Operação |
|:------:|----------|
| ✅ | CREATE_SHIPPING_OPTION |
| ✅ | UPDATE_SHIPPING_OPTION |
| ✅ | DELETE_SHIPPING_OPTION |
| ✅ | REACTIVATE_SHIPPING_OPTION |

Todas as operações acima são persistidas na tabela:

audit_logs

Campos registrados:

- username
- action
- timestamp

---

# Integrações

## Carrier

Responsável por:

- validar existência da transportadora;
- validar se está ativa;
- relacionamento **1:N** com ShippingOption.

---

## Order

Integração futura para cálculo do frete.

Fluxo previsto

```text
Order
      ↓
Carrier
      ↓
ShippingOption
```

---

## Audit

Responsável por registrar todas as operações do módulo.

---

# Arquitetura

```text
Controller
        ↓
Service
        ↓
Repository
        ↓
Entity
```

Os mapeamentos entre Entity e DTO são realizados pelo **ShippingOptionMapper**.

---

# Fluxo Implementado

## Cadastro

```text
POST /shipping-options
        ↓
Usuário autenticado
        ↓
Valida Carrier
        ↓
Verifica Carrier ativo
        ↓
Normaliza serviceName
        ↓
Valida duplicidade
        ↓
Cria ShippingOption
        ↓
Persistência
        ↓
CREATE_SHIPPING_OPTION
```

---

## Consulta

```text
GET /shipping-options
        ↓
Paginação
        ↓
Ordenação
        ↓
Conversão para DTO
        ↓
Resposta
```

---

## Atualização

```text
PUT /shipping-options/{id}
        ↓
Localiza registro
        ↓
Valida duplicidade
        ↓
Atualiza dados
        ↓
Persistência
        ↓
Auditoria
```

---

## Soft Delete

```text
DELETE /shipping-options/{id}
        ↓
Localiza registro
        ↓
Valida ativo
        ↓
active = false
        ↓
Persistência
        ↓
Auditoria
```

---

## Reativação

```text
PATCH /shipping-options/{id}/reactivate
        ↓
Localiza registro
        ↓
Valida inativo
        ↓
active = true
        ↓
Persistência
        ↓
Auditoria
```

---

# Controle de Acesso

```text
Request
   ↓
JWT Authentication
   ↓
Permission Validation
   ↓
Controller
   ↓
Service

# Roadmap do Módulo

## Funcionalidades

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Cadastro |
| ✅ | Consulta por ID |
| ✅ | Listagem paginada |
| ✅ | Atualização |
| ✅ | Soft Delete |
| ✅ | Reativação |
| ✅ | Controle de permissões |
| ✅ | Auditoria |
| ✅ | Validações de entrada |

---

## Comercial

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Faixas de CEP |
| ⏳ | Peso mínimo e máximo |
| ⏳ | Dimensões |
| ⏳ | Valor mínimo |
| ⏳ | Valor máximo |

---

## Integrações Futuras

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Correios |
| ⏳ | Jadlog |
| ⏳ | J&T Express |
| ⏳ | Loggi |
| ⏳ | Melhor Envio |
| ⏳ | Frenet |

# Status Atual

🟢 Módulo concluído.

Implementado:

✅ CRUD completo  
✅ Segurança por permissões  
✅ Soft Delete  
✅ Reativação  
✅ Auditoria  
✅ Validações  
✅ Documentação técnica