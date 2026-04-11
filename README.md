# 🦟 Sistema de Detetização - Mensal 2

Projeto de sistema gerenciador para empresa de detetização (pest control) desenvolvido como assessoria do terceiro período do curso de Engenharia de Software na Universidade América Latina (UNIMERICA).

## 📋 Sumário

- [Visão Geral](#-visão-geral)
- [Tecnologias](#-tecnologias)
- [Requisitos](#-requisitos)
- [Setup & Configuração](#-setup--configuração)
- [Como Executar](#-como-executar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Funcionalidades](#-funcionalidades)
- [Validações de Dados](#-validações-de-dados)
- [Arquitetura](#-arquitetura)
- [Padrões de Design](#-padrões-de-design)
- [Banco de Dados](#-banco-de-dados)
- [Requisitos Mensal 2](#-requisitos-mensal-2---status)

---

## 🎯 Visão Geral

Sistema desktop em Java que gerencia clientes, funcionários, serviços e relatórios para uma empresa de detetização. O sistema utiliza:

- **JPA/Hibernate** para mapeamento objeto-relacional
- **PostgreSQL** para persistência de dados
- **Flyway** para versionamento de banco de dados
- **Maven** para build e dependências
- **Menu interativo** para interface com usuário

### Objetivo Principal

Fornecer uma solução completa para:
- ✅ Cadastro e gestão de clientes
- ✅ Gestão de funcionários (com cargos: GERENTE e FUNCIONÁRIO)
- ✅ Agendamento e acompanhamento de serviços
- ✅ Alocação de funcionários em serviços
- ✅ Relatórios de faturamento e agenda
- ✅ Histórico de operações e triggers automáticos

---

## 🛠 Tecnologias

### Backend
- **Java 21** - Linguagem de programação
- **JPA (Jakarta Persistence)** - Mapeamento objeto-relacional
- **Hibernate 6.x** - Implementação JPA
- **PostgreSQL 12+** - Banco de dados relacional
- **Flyway 9.x** - Versionamento e migrações de BD

### Build & Dependências
- **Maven 3.9+** - Gerenciador de dependências
- **JUnit** - Testes unitários (opcional)

### IDE
- **IntelliJ IDEA** - Recomendado
- **VS Code** - Alternativa
- **Eclipse** - Suportado

---

## ✅ Requisitos

### Sistema
- **Java JDK 21+** instalado
- **Maven 3.9+** instalado e configurado no PATH
- **PostgreSQL 12+** instalado e rodando
- **Git** para controle de versão

### Banco de Dados
- PostgreSQL rodando na máquina (padrão: localhost:3006)
- Credenciais de acesso (usuário: `postgres`)
- Banco de dados chamado `detetizadora_master`

### Verificar Instalação

```bash
# Java
java -version

# Maven
mvn -version

# PostgreSQL (deve estar rodando)
psql --version
```

---

## 🚀 Setup & Configuração

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/ProjetoMensal-TerceiroPeriodo.git
cd ProjetoMensal-TerceiroPeriodo/Mensal1
```

### 2. Configurar Banco de Dados

**No PostgreSQL, execute:**

```sql
-- Criar usuário (se não existir)
CREATE USER postgres WITH PASSWORD 'asdwsad';

-- Criar banco de dados
CREATE DATABASE detetizadora_master OWNER postgres;

-- Dar privilégios
GRANT ALL PRIVILEGES ON DATABASE detetizadora_master TO postgres;
```

### 3. Configurar Arquivo de Persistência

O arquivo `persistence.xml` contém as credenciais do banco. **Não comitar este arquivo!**

```bash
# Copiar arquivo de exemplo
cp src/main/resources/META-INF/persistence.xml.example \
   src/main/resources/META-INF/persistence.xml
```

**Editar `persistence.xml` com suas credenciais:**

```xml
<!-- Linha 16: ajustar URL, usuário e senha -->
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:postgresql://localhost:3006/detetizadora_master"/>
<property name="jakarta.persistence.jdbc.user" value="seu_usuario"/>
<property name="jakarta.persistence.jdbc.password" value="sua_senha"/>
```

**Também em `FlywayConfig.java`:**

```java
// Linha 10-11
.dataSource("jdbc:postgresql://localhost:3006/detetizadora_master", 
            "seu_usuario", "sua_senha")
```

### 4. Instalar Dependências

```bash
mvn clean install
```

Isso irá:
- ✅ Baixar todas as dependências
- ✅ Compilar o código
- ✅ Executar as migrações Flyway (criar tabelas)
- ✅ Criar arquivo JAR final

---

## ▶ Como Executar

### Opção 1: Via IDE (IntelliJ IDEA)

1. Abrir projeto em IntelliJ
2. Navegar até `Main.java`
3. Clicar com botão direito → "Run 'Main.main()'"

### Opção 2: Via Maven

```bash
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"
```

### Opção 3: Via JAR

```bash
# Build
mvn clean package

# Executar
java -jar target/Mensal1-1.0-SNAPSHOT.jar
```

---

## 📁 Estrutura do Projeto

```
Mensal1/
├── src/
│   ├── main/
│   │   ├── java/br/edu/uniamerica/projetomensal/
│   │   │   ├── Main.java                          # Ponto de entrada
│   │   │   ├── config/
│   │   │   │   ├── FlywayConfig.java              # Configuração migrações
│   │   │   │   └── PersistenceManager.java        # Gerenciador EntityManager
│   │   │   ├── model/                              # Entidades JPA
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Funcionario.java
│   │   │   │   ├── Servico.java
│   │   │   │   └── enums/
│   │   │   │       ├── Cargo.java                 # GERENTE, FUNCIONARIO
│   │   │   │       └── Status.java                # ATIVO, INATIVO, etc
│   │   │   ├── repository/                         # Camada de acesso a dados
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   ├── FuncionarioRepository.java
│   │   │   │   └── ServicoRepository.java
│   │   │   ├── service/                            # Camada de lógica de negócio
│   │   │   │   ├── ClienteService.java
│   │   │   │   ├── FuncionarioService.java
│   │   │   │   └── ServicoService.java
│   │   │   ├── menu/                               # Camada de apresentação
│   │   │   │   ├── MenuPrincipal.java
│   │   │   │   ├── ClienteMenu.java
│   │   │   │   ├── FuncionarioMenu.java
│   │   │   │   ├── ServicoMenu.java
│   │   │   │   └── RelatorioMenu.java
│   │   │   ├── relatorio/                          # Geração de relatórios
│   │   │   │   ├── RelatorioCliente.java
│   │   │   │   ├── RelatorioFuncionario.java
│   │   │   │   └── RelatorioServico.java
│   │   │   ├── utils/
│   │   │   │   └── InputUtils.java                # Validação de entradas
│   │   │   ├── banco/
│   │   │   │   └── script.sql
│   │   │   └── interfaces/
│   │   │       └── Crud.java                      # Interface CRUD
│   │   └── resources/
│   │       ├── db/migration/                       # Scripts Flyway
│   │       │   ├── V1__criar_tabelas.sql
│   │       │   ├── V2__criar_triggers.sql
│   │       │   ├── V3__dados_iniciais.sql
│   │       │   ├── V4__joins_consultas.sql
│   │       │   └── V5__ajustes_jpa.sql
│   │       └── META-INF/
│   │           ├── persistence.xml                # ⚠️ IGNORAR NO GIT
│   │           └── persistence.xml.example        # ✅ Template para compartilhar
│   └── test/                                       # Testes (se houver)
├── target/                                         # Compilados (ignore)
├── pom.xml                                         # Dependências Maven
├── .gitignore                                      # Arquivos a ignorar
├── README.md                                       # Este arquivo
└── CONFIGURACAO.md                                 # Guia de configuração
```

---

## ✨ Funcionalidades

### 1️⃣ Gestão de Clientes
- ✅ Cadastrar cliente (CNPJ 14 dígitos ou CPF 11 dígitos)
- ✅ Listar todos os clientes
- ✅ Buscar cliente por ID
- ✅ Atualizar dados do cliente
- ✅ Remover cliente (com confirmação)
- ✅ Status: ATIVO ou INATIVO

### 2️⃣ Gestão de Funcionários
- ✅ Cadastrar funcionário
- ✅ Listar funcionários ativos
- ✅ Listar todos os funcionários
- ✅ Buscar funcionário por ID
- ✅ Atualizar dados do funcionário
- ✅ Remover funcionário (com confirmação)
- ✅ Cargos: GERENTE ou FUNCIONARIO
- ✅ Salário em double (R$)
- ✅ Endereço como campo texto
- ✅ Status automático: ATIVO ao cadastrar

### 3️⃣ Gestão de Serviços
- ✅ Cadastrar serviço (vinculado a cliente)
- ✅ Listar serviços
- ✅ Buscar serviço por ID
- ✅ Atualizar serviço (nome, descrição, data, valor, status)
- ✅ Remover serviço
- ✅ Status: INATIVO, AGENDADO, EM_ANDAMENTO, CONCLUIDO
- ✅ Alocação de múltiplos funcionários por serviço (ManyToMany)
- ✅ Valor do serviço (POSITIVO obrigatório)

### 4️⃣ Relatórios
- ✅ Relatório de clientes com contagem
- ✅ Relatório de funcionários com salários
- ✅ Relatório de faturamento
  - Relatório geral de serviços
  - Relatório por período (data inicial e final)
  - Resumo de agenda (hoje, futuro, atrasados)
  - Serviço mais lucrativo
  - Total arrecadado

### 5️⃣ Banco de Dados
- ✅ 6 tabelas principais
- ✅ Relacionamentos (OneToMany, ManyToOne, ManyToMany)
- ✅ 3 Triggers automáticos
- ✅ 4 tipos de JOINs diferentes
- ✅ Versionamento com Flyway

---

## 🛡 Validações de Dados

### Documento (CPF/CNPJ)
- ✅ CPF: exatamente 11 dígitos
- ✅ CNPJ: exatamente 14 dígitos
- ✅ Remove automaticamente: pontos, barras, hífens
- ✅ Aceita: `000.000.000-00` e `00.000.000/0000-00`

**Exemplos válidos:**
- CPF: `123.456.789-10` → `12345678910`
- CNPJ: `12.345.678/0001-95` → `12345678000195`

### Data (DD/MM/AAAA)
- ✅ Formato: `DD/MM/AAAA` com `/`
- ✅ **Valida se data existe** (rejeita 12/42/5203, 30/02/2026)
- ✅ Trata automaticamente exceções
- ✅ Pede para digitar novamente se inválida

**Exemplos:**
- ✅ `25/12/2026` - Válida
- ❌ `12/42/5203` - Inválida (mês 42)
- ❌ `30/02/2026` - Inválida (fevereiro tem 28 dias)

### Valor de Serviço
- ✅ **Deve ser maior que 0** (R$ > 0)
- ✅ Aceita: `100.50` ou `100,50`
- ✅ Rejeita: `0`, `-50`, valores negativos
- ✅ Pede para digitar novamente se inválido

### Telefone
- ✅ 10 ou 11 dígitos
- ✅ Apenas números
- ✅ Exemplo: `1133334444` ou `11999998888`

### Email
- ✅ Formato válido: `usuario@dominio.com`
- ✅ Validação regex: `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`

### Entrada Geral
- ✅ Campo vazio → mensagem de erro → pede novamente
- ✅ Entrada inválida → mensagem específica → pede novamente
- ✅ Loop até entrada válida

---

## 🏗 Arquitetura

### Padrão MVC (Model-View-Controller)

```
┌─────────────────┐
│  Menu (View)    │  ← Interface com usuário
├─────────────────┤
│  Service (C)    │  ← Lógica de negócio
├─────────────────┤
│  Repository (M) │  ← Acesso a dados
├─────────────────┤
│  Entity (Model) │  ← Objetos de negócio
├─────────────────┤
│  Database       │  ← PostgreSQL
└─────────────────┘
```

### Fluxo de Operação

1. **Menu** (ClienteMenu, ServicoMenu) - Captura entrada do usuário
2. **InputUtils** - Valida e formata entrada
3. **Service** (ClienteService) - Aplica regras de negócio
4. **Repository** - Acessa banco de dados via JPA
5. **Banco de Dados** - Persiste dados e executa triggers

---

## 📐 Padrões de Design

### 1. **Repository Pattern**
- Abstração da camada de dados
- Cada entidade tem seu repositório
- Isolamento de queries SQL

### 2. **Service Layer Pattern**
- Lógica de negócio centralizada
- Validações ocorrem aqui
- Tratamento de exceções

### 3. **DAO (Data Access Object)**
- Classes Repository implementam DAO
- Métodos padronizados: salvar, buscar, listar, excluir

### 4. **Entity Pattern (JPA)**
- Mapeamento objeto-relacional automático
- Anotações `@Entity`, `@Column`, `@OneToMany`, etc
- Hibernate gerencia relacionamentos

### 5. **Enum Pattern**
- `Status`: ATIVO, INATIVO, AGENDADO, EM_ANDAMENTO, CONCLUIDO
- `Cargo`: GERENTE, FUNCIONARIO
- Segurança de tipo + valores controlados

### 6. **Singleton (Implícito)**
- `PersistenceManager`: uma instância de EntityManager por aplicação
- `FlywayConfig`: migrações executadas uma única vez na startup

---

## 📊 Banco de Dados

### Tabelas

1. **clientes** - Informações de clientes
2. **funcionarios** - Funcionários da empresa
3. **servicos** - Serviços agendados
4. **funcionario_servico** - Relacionamento ManyToMany
5. **historico_servicos** - Log de serviços completados (Trigger)
6. **agenda_servicos** - Controle de agenda (Trigger)

### Triggers (3 total)

1. **fn_registrar_historico_servico** - Registra conclusão de serviço
2. **fn_validar_status_funcionario** - Valida status de funcionários
3. **fn_atualizar_agenda_servico** - Atualiza status de agenda

### JOINs Suportados

- ✅ INNER JOIN
- ✅ LEFT JOIN
- ✅ RIGHT JOIN
- ✅ FULL OUTER JOIN

---

## ⚠️ Arquivos Sensíveis (não comitar)

```
❌ NÃO COMITAR:
- persistence.xml (credenciais banco)
- .env (variáveis de ambiente)
- application-dev.properties
- credentials.json
- target/ (compilados)
- .idea/ (IDE)

✅ SEMPRE COMITAR:
- persistence.xml.example (template)
- src/main/resources/db/migration/ (scripts)
- README.md
- pom.xml
- .gitignore
```

---

## 🐛 Troubleshooting

### Erro: "Connection refused" no PostgreSQL

```
Solução:
1. Verificar se PostgreSQL está rodando
2. Verificar porta (padrão: 5432, projeto usa 3006)
3. Verificar credenciais em persistence.xml
4. Executar: sudo systemctl start postgresql (Linux)
```

### Erro: "Table not found"

```
Solução:
1. Flyway não executou as migrações
2. Limpar: mvn clean
3. Reinstalar: mvn clean install
4. Verificar se BD existe: CREATE DATABASE detetizadora_master
```

### Erro: "Invalid value for MonthOfYear"

```
Solução:
1. Data inválida digitada (ex: 12/42/5203)
2. InputUtils.lerData() agora rejeita automaticamente
3. Digitar data válida no formato DD/MM/AAAA
```

---

# 📋 Requisitos Mensal 2 - Status

### ✅ Critério I: Banco de Dados
- ✅ Tabelas criadas (CREATE)
- ✅ CRUD completo para cada tabela
- ✅ 4 tipos de JOINs implementados
- ✅ 3 Triggers funcionais

### ✅ Critério II: Diagramas
- ✅ Diagrama ER conceitual (Peter Chen)
- ✅ Diagrama ER lógico (pé-de-galinha)
- ✅ Diagrama de Classes UML
- ✅ Diagrama de Casos de Uso

### ✅ Critério III: JPA/Hibernate
- ✅ Mapeamento objeto-relacional
- ✅ Relacionamentos: OneToMany, ManyToOne, ManyToMany
- ✅ Flyway para versionamento de BD
- ✅ `hbm2ddl.auto = update`

### ✅ Critério IV: Programação Orientada a Objetos
- ✅ Entidades com anotações JPA
- ✅ Camada Repository com padrão DAO
- ✅ Camada Service com lógica de negócio
- ✅ Try-catch e tratamento de exceções
- ✅ Enums para valores constantes
- ✅ Herança e polimorfismo (implícito em Service)

### ✅ Regras de Negócio (4 implementadas)
1. ✅ Validação de documento (CPF/CNPJ válido)
2. ✅ Validação de data existente (DD/MM/AAAA válido)
3. ✅ Valor de serviço > 0 (positivo obrigatório)
4. ✅ Alocação de funcionários em serviços (ManyToMany)

---

# 👥 Time

- **Adriano Ramos**
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

**Status do Projeto**: ✅ **CONCLUÍDO E ENTREGUE**

Última atualização: 10 de Abril de 2026

---

