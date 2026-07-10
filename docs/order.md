# Módulo Order

## Objetivo

Responsável pelo gerenciamento completo dos pedidos realizados pelos clientes da plataforma.

O módulo controla todo o ciclo de vida de um pedido, desde sua criação até sua conclusão ou cancelamento, integrando produtos, estoque, pagamentos e auditoria.

---

# Segurança

O gerenciamento de pedidos pertence ao Backoffice da aplicação.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Criar Pedido | ✅ | ✅ | ❌ |
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

- Usuário autenticado.
- Pedido deve possuir pelo menos um item.
- Produto deve existir.
- Produto deve estar ativo.
- Produto deve possuir estoque suficiente.
- O valor total será calculado automaticamente.
- O número do pedido será gerado automaticamente.
- Registrar auditoria (`CREATE_ORDER`).
- Associa automaticamente o usuário autenticado ao pedido.

---

## Consulta

### USER

- Pode visualizar apenas seus próprios pedidos.

### EMPLOYEE

- Pode visualizar todos os pedidos.

### ADMIN

- Pode visualizar todos os pedidos.

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

Não será permitido retornar para um status anterior.

---

## Cancelamento

### USER

Pode cancelar apenas pedidos com status:

- PENDING

### EMPLOYEE

Pode cancelar conforme regras de negócio.

### ADMIN

Pode cancelar qualquer pedido permitido pelas regras do sistema.

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
```

---

## Consultar Pedido

```
GET /orders/{id}
```

---

## Atualizar Status

```
PATCH /orders/{id}/status
```

---

## Cancelar Pedido

```
PATCH /orders/{id}/cancel
```

---

# Auditoria

Operações previstas

- CREATE_ORDER
- UPDATE_ORDER_STATUS
- CANCEL_ORDER

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

# Roadmap do Módulo

## Funcionalidades

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Cadastro de pedido |
| ⏳ | Listagem paginada |
| ⏳ | Consulta por ID |
| ⏳ | Atualização de status |
| ⏳ | Cancelamento |

---

## Estoque

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Baixa automática de estoque |
| ⏳ | Devolução automática em cancelamentos |
| ⏳ | Histórico de movimentações |

---

## Comercial

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Número sequencial do pedido |
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
| ⏳ | Endereço de entrega |
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