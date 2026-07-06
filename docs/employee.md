# Employee

## Descrição

O módulo **Employee** é responsável pelo gerenciamento dos funcionários da plataforma.

Somente usuários com perfil **ADMIN** possuem permissão para criar, editar, desativar e reativar funcionários.

O cadastro de funcionários é independente do cadastro de usuários comuns.

---

# Endpoints

## Criar funcionário

### Endpoint

```http
POST /employees
```

### Permissão

```text
CREATE_EMPLOYEE
```

### Request Body

```json
{
  "name": "Marina Amorim",
  "email": "marina@email.com",
  "birthDate": "1995-05-15",
  "gender": "FEMALE",
  "username": "marina",
  "password": "123456"
}
```

### Retorno

```http
201 Created
```

---

## Listar funcionários

### Endpoint

```http
GET /employees
```

### Permissão

```text
READ_EMPLOYEE
```

### Paginação

```http
GET /employees?page=0&size=10
```

### Retorno

```json
{
  "content": [
    {
      "id": "uuid",
      "name": "Marina Amorim",
      "email": "marina@email.com",
      "birthDate": "1995-05-15",
      "gender": "FEMALE",
      "active": true,
      "username": "marina"
    }
  ]
}
```

---

## Buscar funcionário por ID

### Endpoint

```http
GET /employees/{id}
```

### Permissão

```text
READ_EMPLOYEE
```

### Retorno

```json
{
  "id": "uuid",
  "name": "Marina Amorim",
  "email": "marina@email.com",
  "birthDate": "1995-05-15",
  "gender": "FEMALE",
  "active": true,
  "username": "marina"
}
```

---

## Atualizar funcionário

### Endpoint

```http
PUT /employees/{id}
```

### Permissão

```text
UPDATE_EMPLOYEE
```

### Request Body

```json
{
  "name": "Marina Souza",
  "birthDate": "1994-10-12",
  "gender": "FEMALE"
}
```

### Campos permitidos

* name
* birthDate
* gender

### Campos protegidos

* email
* username
* password
* role
* permissions

### Retorno

```http
200 OK
```

---

## Desativar funcionário

### Endpoint

```http
DELETE /employees/{id}
```

### Permissão

```text
DELETE_EMPLOYEE
```

### Regra

Realiza Soft Delete.

```text
active = false
```

### Retorno

```http
204 No Content
```

---

## Reativar funcionário

### Endpoint

```http
PATCH /employees/{id}/reactivate
```

### Permissão

```text
REACTIVATE_EMPLOYEE
```

### Regra

Reativa o funcionário.

```text
active = true
```

### Retorno

```http
200 OK
```

---

# Auditoria

Todos os endpoints administrativos registram eventos na tabela `audit_log`.

### Eventos registrados

* CREATE_EMPLOYEE
* UPDATE_EMPLOYEE
* DELETE_EMPLOYEE
* REACTIVATE_EMPLOYEE

---

# Segurança

Somente usuários com as permissões adequadas podem acessar os endpoints.

### Permissões do módulo

* CREATE_EMPLOYEE
* READ_EMPLOYEE
* UPDATE_EMPLOYEE
* DELETE_EMPLOYEE
* REACTIVATE_EMPLOYEE

---

# Melhorias futuras

* Alteração de senha do funcionário.
* Alteração de username.
* Alteração de perfil (Role).
* Busca por nome.
* Filtros por status (ativo/inativo).
* Filtros por data de cadastro.
* Exportação de funcionários.
* Histórico de alterações.
* Bloqueio temporário de funcionários.

---

# Status do módulo

## Funcionalidades concluídas

* Cadastro de funcionário.
* Listagem paginada.
* Busca por ID.
* Atualização.
* Soft Delete.
* Reativação.
* Auditoria.
* Controle de permissões.
