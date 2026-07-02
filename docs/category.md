# Módulo Category

## Objetivo

O módulo **Category** é responsável pelo gerenciamento das categorias de produtos do sistema. Todas as operações seguem o padrão adotado no projeto, utilizando autenticação JWT, auditoria, tratamento de exceções e exclusão lógica (Soft Delete).

---

# Entidade

## Category

| Campo       | Tipo          | Observação                       |
| ----------- | ------------- | -------------------------------- |
| id          | UUID          | Identificador da categoria       |
| name        | String        | Nome da categoria (único)        |
| description | String        | Descrição da categoria           |
| active      | Boolean       | Indica se a categoria está ativa |
| createdAt   | LocalDateTime | Data de criação                  |
| updatedAt   | LocalDateTime | Data da última alteração         |

---

# Regras de Negócio

## Cadastro

* Apenas usuários ADMIN podem cadastrar categorias.
* O nome da categoria deve ser único.
* A comparação do nome ignora letras maiúsculas/minúsculas.
* Espaços no início e fim do nome são removidos automaticamente.
* createdAt e updatedAt são preenchidos automaticamente.
* A categoria é criada com active = true.
* Registrar auditoria (CREATE_CATEGORY).

---

## Listagem

* Usuários ADMIN e USER podem listar categorias.
* Apenas categorias ativas são retornadas.
* A listagem é paginada.
* Ordenação padrão por nome (ASC).
* Não registra auditoria.

---

## Alteração

* Apenas ADMIN pode alterar categorias.
* A categoria deve existir.
* O nome permanece único.
* Não é permitido alterar:

  * id
  * active
  * createdAt
* updatedAt é atualizado automaticamente.
* Registrar auditoria (UPDATE_CATEGORY).

---

## Exclusão

* Apenas ADMIN pode excluir categorias.
* A exclusão é lógica (Soft Delete).
* active passa para false.
* updatedAt é atualizado automaticamente.
* Registrar auditoria (DELETE_CATEGORY).

---

## Reativação

* Apenas ADMIN pode reativar categorias.
* A categoria pode estar ativa ou inativa.
* Caso já esteja ativa, retornar erro.
* active passa para true.
* updatedAt é atualizado automaticamente.
* Registrar auditoria (REACTIVATE_CATEGORY).

---

# Segurança

| Operação | Permissão    |
| -------- | ------------ |
| Criar    | ADMIN        |
| Listar   | ADMIN e USER |
| Alterar  | ADMIN        |
| Excluir  | ADMIN        |
| Reativar | ADMIN        |

---

# Auditoria

As seguintes ações são registradas na tabela audit_log:

* CREATE_CATEGORY
* UPDATE_CATEGORY
* DELETE_CATEGORY
* REACTIVATE_CATEGORY

---

# Tratamento de Erros

## 400 - Bad Request

* Categoria já cadastrada.
* Categoria já está ativa.
* Dados inválidos.

---

## 401 - Unauthorized

* Token JWT ausente, vencido ou inválido.

---

## 403 - Forbidden

* Usuário sem permissão para executar a operação.

---

## 404 - Not Found

* Categoria não encontrada.

---

# Endpoints

## Criar categoria

POST /categories

---

## Listar categorias

GET /categories

Suporta paginação:

* page
* size
* sort

Exemplo:

GET /categories?page=0&size=10&sort=name,asc

---

## Alterar categoria

PUT /categories/{id}

---

## Excluir categoria (Soft Delete)

DELETE /categories/{id}

---

## Reativar categoria

PATCH /categories/{id}/reactivate

---

# Padrões adotados

* Soft Delete.
* Reactivate por endpoint próprio.
* Paginação utilizando Spring Data.
* Conversão Entity ↔ DTO via Mapper.
* Validação de unicidade ignorando maiúsculas/minúsculas.
* Remoção de espaços excedentes no nome da categoria.
* Tratamento centralizado de exceções.
* Auditoria de todas as operações de escrita.
* Código organizado em Controller, Service, Repository, DTO, Mapper e Entity.

---

# Evoluções Futuras

* Impedir exclusão de categorias que possuam produtos vinculados.
* Pesquisa por nome.
* Filtro por status (ativas/inativas).
* Ordenações personalizadas.
* Upload de imagem para categoria.
* Cache de categorias mais utilizadas.
