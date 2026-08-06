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
- Gerenciar transportadoras (Carrier)
- Gerenciar opções de frete (ShippingOption)

---

### EMPLOYEE

Responsável pela operação do e-commerce.

Permissões:

- Gerenciar categorias
- Gerenciar produtos
- Gerenciar opções de frete
- Atualizar pedidos

---

### USER

Cliente do e-commerce.

Permissões:

- Gerenciar o próprio perfil
- Gerenciar seus endereços
- Realizar compras

# Sincronização Automática de Permissões

O sistema realiza automaticamente a sincronização das permissões cadastradas para cada Role durante a inicialização da aplicação.

Fluxo:

```text
Permission (enum)
        ↓
PermissionInitializer
        ↓
permissions (tabela)
        ↓
PermissionSynchronizer
        ↓
auth_permissions
```

## Funcionamento

Quando uma nova permissão é adicionada ao sistema:

1. A permissão é criada automaticamente na tabela `permissions` pelo `PermissionInitializer`.
2. O `PermissionSynchronizer` percorre todos os usuários cadastrados.
3. Cada usuário recebe automaticamente as permissões definidas para sua `Role`.
4. Não é mais necessário executar comandos SQL manuais para atualizar a tabela `auth_permissions`.

## Benefícios

- Elimina sincronizações manuais no banco de dados.
- Garante que usuários existentes recebam novas permissões automaticamente.
- Mantém as permissões consistentes com as definições do enum `Role`.
- Facilita a evolução do sistema com novos módulos.

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