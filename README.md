
---

# 🏢 Sistema de Gestão de Serviços – Detetizadora

Este projeto tem como objetivo desenvolver um sistema de gestão de chamados e serviços para uma empresa de detetização.

O sistema permitirá:

* Cadastro de clientes (empresas)
* Cadastro de funcionários
* Cadastro de serviços
* Agendamento de atendimentos
* Controle de status dos serviços
* Geração de relatórios

O projeto está sendo desenvolvido em Java, utilizando princípios de Programação Orientada a Objetos e organização em camadas.

---

# 📂 Estrutura do Projeto

O sistema está organizado seguindo uma separação de responsabilidades:

### 📁 model

Contém as classes que representam as entidades do sistema.
Exemplo:

* Cliente
* Funcionario
* Servico
* Enums (Cargo, Status, etc.)

Essas classes representam os dados da aplicação.

---

### 📁 repository

Responsável pelo armazenamento dos dados em memória.
Simula o comportamento de um banco de dados.

Exemplo:

* ClienteRepository
* FuncionarioRepository
* ServicoRepository

Essa camada apenas salva, busca, lista e remove dados.

---

### 📁 service

Contém as regras de negócio do sistema.

É responsável por:

* Validar dados
* Aplicar regras
* Gerenciar IDs
* Controlar operações antes de acessar o repository

Exemplo:

* ClienteService
* FuncionarioService
* ServicoService

---

### 📁 menu (ou view)

Responsável pela interação com o usuário.

Aqui ficam:

* Scanner
* Menus
* Entrada e saída de dados pelo terminal

Exemplo:

* ServicoMenu
* ClienteMenu

---

### 📁 interfaces

Contém interfaces utilizadas no projeto, como o contrato genérico de operações CRUD.

Exemplo:

* Crud<T>

---

### 📁 utilidades (util)

Contém classe utilitaria, para validações de entradas e formatações das mesmas.

Exemplo:

* Menu Servico
* Menu Relatorios

---

# 🧠 Conceitos Aplicados

* Programação Orientada a Objetos
* Encapsulamento
* Enum
* Interface
* Generics
* Separação em camadas
* Organização arquitetural

---

# 📋 Progresso das Entregas

## 🎯 Mensal 1 — ✅ CONCLUÍDO
**Status:** Código Java funcional, sem banco de dados.

Implementações:
- ✅ Classes de modelo (Cliente, Funcionário, Serviço)
- ✅ Repositories em memória (ArrayList)
- ✅ Services com validações
- ✅ Menus interativos com Scanner
- ✅ Relatórios básicos
- ✅ Enums (Status, Cargo)
- ✅ Interface genérica Crud<T>

---

## 🚀 Mensal 2 — 🔄 EM ANDAMENTO

### Critério I — SQL Puro ✅ (95% feito)
**Objetivo:** Demonstrar conhecimento de SQL com tabelas, CRUD, JOINs e triggers.

**Localização:** `banco/script.sql` e `src/main/resources/db/migration/V1__criar_tabelas.sql`

#### Tabelas criadas:
```
├── clientes
├── funcionarios
├── servicos
└── agenda
```

#### CRUD implementado:
- ✅ **CREATE** (INSERT)
- ✅ **READ** (SELECT com filtros)
- ✅ **UPDATE**
- ✅ **DELETE** (respeitando Foreign Keys)

#### JOINs implementados (4 tipos):
1. **INNER JOIN** — Serviços com cliente
2. **LEFT JOIN** — Serviços sem funcionário obrigatório
3. **RIGHT JOIN** — Funcionários sem agendamento
4. **FULL JOIN** — Clientes e serviços com ou sem correspondência

#### Triggers implementados (3):
1. **trg_historico_servico** — Registra serviços concluídos em tabela de histórico
2. **trg_validar_status_funcionario** — Bloqueia cadastro de funcionário INATIVO
3. **trg_atualizar_agenda_servico** — Atualiza status da agenda quando serviço é concluído

---

### Critério II — Diagramas ✅
- ✅ Diagrama ER conceitual (Peter Chen)
- ✅ Diagrama ER lógico (pé-de-galinha)
- ✅ Diagrama de Classes UML
- ✅ Diagrama de Casos de Uso

---

### Critério III — JPA + Flyway 🔜 (próximo)
**Objetivo:** Refatorar repositories para usar Hibernate + JPA.

**Dependências adicionadas ao pom.xml:**
- `postgresql:42.7.3` — Driver JDBC
- `hibernate-orm:6.4.4.Final` — Implementação JPA
- `flyway-core:10.10.0` — Gerenciador de migrations

**Próximos passos:**
1. Criar anotações @Entity, @Column, @ManyToOne, @OneToMany
2. Refatorar repositories para extends JpaRepository
3. Configurar banco PostgreSQL no `application.properties`
4. Mover script SQL para migrations do Flyway

---

### Critério IV — Regras de Negócio 🔜 (depois do III)
**Objetivo:** Implementar lógica de negócio e tratamento de exceções.

---

### Critério V — Entrega 🔜 (final)
Documentação em ABNT, apresentação.

---

# 👥 Time

- **Adriano**
- **Luan** 
- **Victor Hugo** 
- **Victor Luis** 

**Mentor:** Riad Younes  
**Orientador:** Victor Matheus

---

# 📚 Recursos

- [PostgreSQL Official Docs](https://www.postgresql.org/docs/)
- [JPA Specification](https://jakarta.ee/specifications/persistence/3.1/)
- [Flyway Docs](https://flywaydb.org/documentation/)
- [Hibernate Docs](https://hibernate.org/orm/documentation/)

---

