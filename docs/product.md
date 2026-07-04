# Módulo Product

## Objetivo

Responsável pelo cadastro e gerenciamento dos produtos disponíveis para venda no e-commerce.

---

# Entidade

## Product

Campos previstos

- id
- name
- description
- sku
- price
- stock
- active
- category
- createdAt
- updatedAt

---

# Regras de Negócio

## Cadastro

- Apenas ADMIN poderá cadastrar produtos.
- Nome obrigatório.
- SKU único.
- Preço maior que zero.
- Estoque inicial maior ou igual a zero.
- Categoria obrigatória.
- Produto inicia ativo.
- Registrar auditoria (CREATE_PRODUCT).

---

## Listagem

- Paginação.
- Ordenação por nome.
- Apenas produtos ativos.

---

## Alteração

- Apenas ADMIN.
- Atualizar updatedAt.
- Registrar auditoria.

---

## Exclusão

- Soft Delete.

---

## Reativação

- Reactivate.

---

# Endpoints previstos

POST /products

GET /products

GET /products/search

PUT /products/{id}

DELETE /products/{id}

PATCH /products/{id}/reactivate

---

# Evoluções Futuras

- Upload de imagens.
- Múltiplas imagens por produto.
- Marca.
- Peso.
- Dimensões.
- Código de barras.
- Desconto.
- Promoções.