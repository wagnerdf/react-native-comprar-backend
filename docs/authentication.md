# Autenticação e Autorização

## Roles

---

- ADMIN administra pessoas e o sistema.
- EMPLOYEE opera o negócio.
- USER consome o serviço.

---

### ADMIN

Responsável pela administração completa do sistema.

Permissões:

- Gerenciar usuários
- Gerenciar funcionários
- Gerenciar categorias
- Gerenciar produtos
- Gerenciar pedidos
- Configurações do sistema

---

### EMPLOYEE

Responsável pela operação do e-commerce.

Permissões:

- Gerenciar categorias
- Gerenciar produtos
- Atualizar pedidos

---

### USER

Cliente do e-commerce.

Permissões:

- Gerenciar o próprio perfil
- Gerenciar seus endereços
- Realizar compras

## Evoluções Futuras

- MANAGER
- STOCK
- FINANCIAL
- SUPPORT

## Fluxo de criação de usuários

ADMIN
↓

Cria EMPLOYEE

EMPLOYEE

↓

Opera o sistema

USER

↓

Auto cadastro