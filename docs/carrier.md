# Módulo Carrier

## Objetivo

Responsável pelo gerenciamento das transportadoras utilizadas pela plataforma.

O módulo centraliza o cadastro das empresas responsáveis pelo transporte dos pedidos e servirá como base para o cálculo de frete, geração de rastreamento e integração com APIs de logística.

---

# Segurança

O gerenciamento de transportadoras pertence ao Backoffice da aplicação.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Criar Transportadora | ✅ | ❌ | ❌ |
| Listar Transportadoras | ✅ | ✅ | ❌ |
| Consultar Transportadora | ✅ | ✅ | ❌ |
| Atualizar Transportadora | ✅ | ❌ | ❌ |
| Excluir (Soft Delete) | ✅ | ❌ | ❌ |
| Reativar | ✅ | ❌ | ❌ |

---

# Entidade

## Carrier

Campos

- id
- name
- active
- createdAt
- updatedAt

---

# Regras de Negócio

## Cadastro

- ✅ Nome obrigatório.
- ✅ Remove espaços no início e fim do nome.
- ✅ Não permite nomes duplicados.
- ✅ Nova transportadora inicia como ativa.

---

## Consulta

### ADMIN

- Pode visualizar todas.

### EMPLOYEE

- Pode visualizar todas.

### USER

- Não possui acesso.

---

## Atualização

- ✅ Permite alterar apenas o nome.
- ✅ Remove espaços extras automaticamente.
- ✅ Não permite duplicidade.

---

## Exclusão

- Exclusão lógica (Soft Delete).

Ao excluir:

- active = false
- updatedAt atualizado.

---

## Reativação

- Altera active = true.
- Atualiza updatedAt.

---

# Endpoints

## Criar

```
POST /carriers
```

**Status:** ✅ Implementado

---

## Listar

```
GET /carriers
```

**Status:** ✅ Implementado

---

## Consultar

```
GET /carriers/{id}
```

**Status:** ✅ Implementado

---

## Atualizar

```
PUT /carriers/{id}
```

**Status:** ✅ Implementado

---

## Excluir

```
DELETE /carriers/{id}
```

**Status:** ✅ Implementado

Soft Delete.

---

## Reativar

```
PATCH /carriers/{id}/reactivate
```

**Status:** ✅ Implementado

---

# Fluxo

```text
POST /carriers
        ↓
Validação do nome
        ↓
Normalização (trim)
        ↓
Verificação de duplicidade
        ↓
Persistência
```

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

O mapeamento entre Entity e DTO é realizado pelo **CarrierMapper**.

---

# Roadmap

## Funcionalidades

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Cadastro |
| ✅ | Listagem |
| ✅ | Consulta |
| ✅ | Atualização |
| ✅ | Soft Delete |
| ✅ | Reativação |

---

## Integrações Futuras

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | ShippingOption |
| ⏳ | Cálculo de frete |
| ⏳ | APIs de transportadoras |
| ⏳ | Código de rastreio |
| ⏳ | Prazo de entrega |
| ⏳ | Tabela de frete |

---

# Dependências

Este módulo é utilizado por:

- ShippingOption
- Order
- Freight Calculator