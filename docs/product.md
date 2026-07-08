# Segurança

O gerenciamento de produtos pertence ao **Backoffice** da aplicação.

As operações são permitidas somente para usuários com as permissões adequadas.

| Operação | ADMIN | EMPLOYEE | USER |
|----------|:-----:|:--------:|:----:|
| Criar Produto | ✅ | ✅ | ❌ |
| Listar Produtos | ✅ | ✅ | ❌ |
| Consultar Produto | ✅ | ✅ | ❌ |
| Atualizar Produto | ✅ | ✅ | ❌ |
| Desativar Produto | ✅ | ✅ | ❌ |
| Reativar Produto | ✅ | ✅ | ❌ |

---

# Módulo Product

## Objetivo

Responsável pelo cadastro, manutenção e gerenciamento dos produtos disponíveis para venda no e-commerce.

O módulo controla todas as operações administrativas do catálogo de produtos, utilizando Soft Delete para preservar o histórico das informações.

---

# Entidade

## Product

Campos implementados

- id
- name
- description
- sku
- barcode
- price
- stock
- minimumStock
- active
- category
- createdAt
- updatedAt

---

# Regras de Negócio

## Cadastro

- Nome obrigatório.
- SKU obrigatório.
- SKU único.
- Categoria obrigatória.
- Categoria deve existir.
- Preço maior que zero.
- Estoque não pode ser negativo.
- Estoque mínimo não pode ser negativo.
- Produto inicia ativo.
- Atualiza datas de criação e alteração.
- Registra auditoria (`CREATE_PRODUCT`).

---

## Listagem

- Apenas produtos ativos.
- Paginação.
- Ordenação.
- Busca por nome.
- Somente usuários autorizados podem consultar.

---

## Alteração

- Produto deve existir.
- Categoria deve existir.
- SKU continua sendo único.
- É permitido manter o próprio SKU.
- Não é permitido utilizar o SKU de outro produto.
- Atualiza `updatedAt`.
- Registra auditoria (`UPDATE_PRODUCT`).

---

## Exclusão

Soft Delete.

O produto não é removido fisicamente do banco.

Regras:

- active = false
- updatedAt = now()

Caso o produto já esteja inativo:

- BusinessException

Registra auditoria (`DELETE_PRODUCT`).

---

## Reativação

Reativa um produto desativado.

Regras:

- active = true
- updatedAt = now()

Caso o produto já esteja ativo:

- BusinessException

Registra auditoria (`REACTIVATE_PRODUCT`).

---

# Endpoints Implementados

## Cadastro

```
POST /products
```

---

## Listagem

```
GET /products
```

Suporta:

- paginação
- ordenação
- busca por nome

---

## Consulta

```
GET /products/{id}
```

---

## Atualização

```
PUT /products/{id}
```

---

## Soft Delete

```
DELETE /products/{id}
```

---

## Reativação

```
PATCH /products/{id}/reactivate
```

---

# Auditoria

Operações auditadas

- CREATE_PRODUCT
- UPDATE_PRODUCT
- DELETE_PRODUCT
- REACTIVATE_PRODUCT

---

# Soft Delete

O módulo utiliza exclusão lógica.

Produtos desativados:

- permanecem no banco;
- não aparecem na listagem principal;
- podem ser reativados posteriormente.


---

# Arquitetura

```
Controller
        ↓
Service
        ↓
Repository
        ↓
Entity
```

Mapeamentos entre Entity e DTO são realizados pelo **ProductMapper**.

---

# Melhorias Futuras

## Funcionalidades

| Status | Funcionalidade |
|:------:|----------------|
| ✅ | Cadastro de produto |
| ✅ | Listagem paginada |
| ✅ | Consulta por ID |
| ✅ | Atualização |
| ✅ | Soft Delete |
| ✅ | Reativação |

---

## Catálogo

| Status | Funcionalidade |
|:------:|----------------|
| 🧪 | Busca por categoria |
| ⏳ | Busca por faixa de preço |
| ⏳ | Busca por código de barras |
| ⏳ | Produtos inativos |
| ⏳ | Produtos em destaque |
| ⏳ | Produtos em promoção |
| ⏳ | Produtos mais vendidos |

---

## Estoque

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Movimentação de estoque |
| ⏳ | Histórico de movimentações |
| ⏳ | Entrada de estoque |
| ⏳ | Saída de estoque |
| ⏳ | Ajuste de inventário |
| ⏳ | Alerta de estoque mínimo |

---

## Produto

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Upload de imagens |
| ⏳ | Múltiplas imagens |
| ⏳ | Marca |
| ⏳ | Peso |
| ⏳ | Dimensões |
| ⏳ | Unidade de medida |
| ⏳ | Código NCM |
| ⏳ | Código de barras alternativo |
| ⏳ | Fornecedor |

---

## Comercial

| Status | Funcionalidade |
|:------:|----------------|
| ⏳ | Controle de custo |
| ⏳ | Margem de lucro |
| ⏳ | Histórico de preços |
| ⏳ | Descontos |
| ⏳ | Promoções |
| ⏳ | Kits de produtos |