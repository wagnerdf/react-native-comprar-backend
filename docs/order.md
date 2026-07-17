# Módulo Order

## Objetivo

Responsável pelo gerenciamento completo dos pedidos realizados pelos clientes da plataforma.

O módulo controla todo o ciclo de vida de um pedido, desde sua criação até sua conclusão ou cancelamento, integrando produtos, estoque, pagamentos e auditoria.

---
# Segurança

O gerenciamento de pedidos pertence ao Backoffice da aplicação.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Criar Pedido | ✅ | ❌ | ✅ |
| Listar Próprios Pedidos | ❌ | ❌ | ✅ |
| Consultar Próprio Pedido | ❌ | ❌ | ✅ |
| Listar Todos os Pedidos | ✅ | ✅ | ❌ |
| Atualizar Status | ✅ | ✅ | ❌ |
| Cancelar Pedido | ✅ | ✅ | ✅* |
| Reativar Pedido | ⏳ | ⏳ | ❌ |

> **Observação:** O usuário poderá cancelar apenas pedidos que ainda estejam com status **PENDING**.

---

# Entidades

## Order

Campos previstos

- id
- orderNumber
- user
- status
- total
- createdAt
- updatedAt

---

## OrderItem

Campos previstos

- id
- order
- product
- quantity
- unitPrice
- subtotal

---

## DeliveryAddress

Snapshot do endereço utilizado no momento da compra.

Campos previstos

- id
- order
- recipientName
- zipCode
- street
- number
- complement
- neighborhood
- city
- state
- reference

---

# Status do Pedido

Fluxo principal

```text
PENDING
    ↓
PROCESSING
    ↓
PAID
    ↓
SHIPPED
    ↓
DELIVERED
```

Fluxo alternativo

```text
PENDING
    ↓
CANCELLED
```

---

# Regras de Negócio

## Cadastro

- ✅ Usuário autenticado.
- ✅ Pedido deve possuir pelo menos um item.
- ✅ Produto deve existir.
- ✅ Produto deve estar ativo.
- ✅ Produto deve possuir estoque suficiente.
- ✅ O valor total será calculado automaticamente.
- ✅ O número do pedido será gerado automaticamente.
- ✅ Registrar auditoria (`CREATE_ORDER`).
- ✅ Associa automaticamente o usuário autenticado ao pedido.
- ✅ Valida o endereço de entrega informado.
- ✅ O endereço deve pertencer ao usuário autenticado.
- ✅ Cria um snapshot do endereço de entrega no momento da compra.
- ✅ O número do pedido é composto por:
  - Prefixo ORD
  - Ano corrente
  - UF do endereço de entrega
  - 3 dígitos aleatórios
  - Sequência do banco de dados

---

## Consulta

### USER

- ✅ Pode visualizar apenas seus próprios pedidos.

### EMPLOYEE

- ✅ Pode visualizar todos os pedidos.

### ADMIN

- ✅ Pode visualizar todos os pedidos.

---

## Atualização de Status

Permitido apenas para EMPLOYEE e ADMIN.

Fluxo permitido

```text
PENDING
    ↓
PROCESSING
    ↓
PAID
    ↓
SHIPPED
    ↓
DELIVERED
```

Não será permitido:

- retornar para um status anterior;
- pular etapas do fluxo;
- alterar pedidos CANCELLED;
- alterar pedidos DELIVERED.

---

## Cancelamento

### USER

Pode cancelar apenas pedidos com status:

- PENDING

### EMPLOYEE

Pode cancelar qualquer pedido com status PENDING.

### ADMIN

Pode cancelar qualquer pedido com status PENDING.

Ao cancelar:

- altera status para CANCELLED;
- devolve estoque (implementação futura);
- registra auditoria (`CANCEL_ORDER`).

---

# Endpoints

## Criar Pedido

```
POST /orders
**Status:** ✅ Implementado
```

---

## Listar Pedidos

```
GET /orders 
**Status:** ✅ Implementado
- Paginação
- Ordenação por data de criação (mais recentes primeiro)
```

---

## Consultar Pedido

```
GET /orders/{id}
**Status:** ✅ Implementado
- USER visualiza apenas seus pedidos.
- ADMIN e EMPLOYEE visualizam qualquer pedido.
- Retorna também o snapshot do endereço de entrega.
```text
PATCH /orders/{id}/status
        ↓
Localiza pedido
        ↓
Valida transição
        ↓
Atualiza status
        ↓
Persistência
```

```text
PATCH /orders/{id}/cancel
        ↓
Localiza pedido
        ↓
Valida permissão
        ↓
Status = PENDING?
        ↓
Altera para CANCELLED
        ↓
Persistência
```

---

## Atualizar Status

```
PATCH /orders/{id}/status
**Status:** ✅ Implementado
- Apenas ADMIN e EMPLOYEE.
- Fluxo sequencial obrigatório.
- Não permite retornar etapas.
- Não permite alterar pedidos CANCELLED ou DELIVERED.
```

---

## Cancelar Pedido

```
PATCH /orders/{id}/cancel
**Status:** ✅ Implementado

- USER cancela apenas pedidos próprios.
- Apenas pedidos com status PENDING.
- ADMIN e EMPLOYEE podem cancelar qualquer pedido PENDING.
```

---

# Auditoria

Operações previstas (pendentes de implementação)

| ⏳ | - CREATE_ORDER
| ⏳ | - UPDATE_ORDER_STATUS
| ⏳ | - CANCEL_ORDER

---

# Integrações

## Product

Responsável por:

- validar existência;
- validar estoque;
- validar produto ativo;
- obter preço;
- calcular subtotal.

---

## User

Responsável por:

- identificar o comprador.
- recuperar usuário autenticado via JWT.

---

## Audit

Responsável por:

- registrar todas as operações.

---

## Estoque

Integrações futuras

- baixa automática de estoque;
- devolução automática em cancelamentos;
- histórico de movimentações.

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

Os mapeamentos entre Entity e DTO serão realizados pelo **OrderMapper**.

---

# Fluxo Implementado

```text
POST /orders
        ↓
Validação do usuário autenticado
        ↓
Validação do endereço de entrega
        ↓
Validação dos produtos
        ↓
Validação do estoque
        ↓
Criação dos itens
        ↓
Cálculo do valor total
        ↓
Criação do DeliveryAddress (snapshot)
        ↓
Geração do número do pedido
        ↓
Persistência
```

```text
GET /orders
        ↓
ADMIN / EMPLOYEE
        ↓
Lista todos os pedidos

USER
        ↓
Lista apenas seus pedidos
```

```text
GET /orders/{id}
        ↓
Pedido existe?
        ↓
Validação de acesso
        ↓
Conversão para DTO
        ↓
Resposta
```
---

# Roadmap do Módulo

## Funcionalidades

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Cadastro de pedido |
| ✅ | Listagem paginada |
| ✅ | Consulta por ID |
| ✅ | Atualização de status |
| ✅ | Cancelamento |

---

## Estoque

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Baixa automática de estoque |
| ✅ | Devolução automática em cancelamentos |
| ✅ | Histórico de movimentações |

---

## Comercial

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Número sequencial do pedido (ORD + Ano + UF + Aleatório + Sequência) |
| ⏳ | Cupons de desconto |
| ⏳ | Frete |
| ⏳ | Cálculo de impostos |
| ⏳ | Observações do pedido |

---

## Pagamentos

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | PIX |
| ⏳ | Cartão de Crédito |
| ⏳ | Cartão de Débito |
| ⏳ | Boleto Bancário |
| ⏳ | Gateway de Pagamentos |

---

## Entrega

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Endereço de entrega (snapshot do endereço utilizado na compra) |
| ⏳ | Código de rastreio |
| ⏳ | Transportadora |
| ⏳ | Prazo de entrega |

---

## Relatórios

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Pedidos por período |
| ⏳ | Produtos mais vendidos |
| ⏳ | Clientes que mais compram |
| ⏳ | Faturamento |
| ⏳ | Ticket médio |