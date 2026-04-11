# ⚡ Quick Reference - Mensal 2

Guia rápido de referência com comandos, endpoints e padrões do projeto.

---

## 📦 Comando Maven Mais Usados

```bash
# Compilar
mvn clean compile

# Instalar (compile + testes + package)
mvn clean install

# Apenas compilar sem testes
mvn clean compile -DskipTests

# Executar aplicação
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"

# Gerar JAR
mvn clean package

# Executar JAR
java -jar target/Mensal1-1.0-SNAPSHOT.jar

# Ver dependências
mvn dependency:tree
```

---

## 🗄 Banco de Dados - Comandos SQL

### Conectar ao PostgreSQL

```bash
# Conectar
psql -U postgres

# Ou especificando banco
psql -U postgres -d detetizadora_master

# Comandos úteis no psql
\l          # Listar bancos
\dt         # Listar tabelas
\d tabela   # Ver estrutura da tabela
\c banco    # Conectar a outro banco
\q          # Sair
```

### Criar Banco (primeira vez)

```sql
CREATE USER postgres WITH PASSWORD 'asdwsad';
CREATE DATABASE detetizadora_master OWNER postgres;
GRANT ALL PRIVILEGES ON DATABASE detetizadora_master TO postgres;
```

### Ver Dados

```sql
SELECT * FROM clientes;
SELECT * FROM funcionarios;
SELECT * FROM servicos;
SELECT * FROM funcionario_servico;
```

### Limpar Dados (CUIDADO!)

```sql
-- Limpar tabelas
DELETE FROM historico_servicos;
DELETE FROM funcionario_servico;
DELETE FROM servicos;
DELETE FROM funcionarios;
DELETE FROM clientes;

-- Resetar sequências
ALTER SEQUENCE clientes_id_seq RESTART WITH 1;
ALTER SEQUENCE funcionarios_id_seq RESTART WITH 1;
ALTER SEQUENCE servicos_id_seq RESTART WITH 1;
```

---

## 📁 Arquivos Chave

### Configuração
| Arquivo | Descrição |
|---------|-----------|
| `pom.xml` | Dependências Maven |
| `persistence.xml` | ⚠️ Credenciais banco (não commitar) |
| `persistence.xml.example` | ✅ Template (commitar) |
| `.gitignore` | Arquivos a ignorar |
| `FlywayConfig.java` | Configuração migrações |

### Modelos
| Arquivo | Descrição |
|---------|-----------|
| `Cliente.java` | Entidade Cliente com @Entity |
| `Funcionario.java` | Entidade Funcionário |
| `Servico.java` | Entidade Serviço |
| `Status.java` | Enum com status |
| `Cargo.java` | Enum com cargos |

### Repositórios (Data Access)
| Arquivo | Métodos |
|---------|---------|
| `ClienteRepository.java` | salvar, buscar, listar, excluir |
| `FuncionarioRepository.java` | salvar, buscar, listar, excluir |
| `ServicoRepository.java` | salvar, buscar, listar, excluir |

### Services (Business Logic)
| Arquivo | Métodos |
|---------|---------|
| `ClienteService.java` | Validações de cliente |
| `FuncionarioService.java` | Validações de funcionário |
| `ServicoService.java` | Validações de serviço |

### Menus (Presentation)
| Arquivo | Funcionalidade |
|---------|---------|
| `MenuPrincipal.java` | Menu principal |
| `ClienteMenu.java` | CRUD Cliente |
| `FuncionarioMenu.java` | CRUD Funcionário |
| `ServicoMenu.java` | CRUD Serviço |
| `RelatorioMenu.java` | Relatórios |

### Utilidades
| Arquivo | Funcionalidade |
|---------|---------|
| `InputUtils.java` | Validação de entradas |

---

## 🎯 Validações Implementadas

### Documento (CPF/CNPJ)
- Método: `InputUtils.lerDocumento()`
- CPF: 11 dígitos
- CNPJ: 14 dígitos
- Remove: `.`, `-`, `/`
- Erro: "O documento deve conter exatamente 11 ou 14 números"

### Data (DD/MM/AAAA)
- Método: `InputUtils.lerData()`
- Formato: `DD/MM/AAAA` com `/`
- **Valida se data existe** (rejeita 30/02, 42/13, etc)
- Erro: "Data inexistente"

### Valor Serviço
- Método: `InputUtils.lerValorServico()`
- Regra: **Deve ser > 0**
- Aceita: `100.50` ou `100,50`
- Erro: "O valor do serviço deve ser maior que 0"

### Telefone
- Método: `InputUtils.lerTelefone()`
- Regra: 10 ou 11 dígitos
- Apenas números
- Erro: "Deve conter entre 10 e 11 números"

### Email
- Método: `InputUtils.lerEmail()`
- Formato: `usuario@dominio.com`
- Regex: `^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$`
- Erro: "Email inválido"

---

## 🏗 Arquitetura em Camadas

```
┌─────────────────────────┐
│  Menu (ClienteMenu)     │  ← lerData(), lerDocumento()
├─────────────────────────┤
│  Service (ClienteServ)  │  ← Validações + Regras
├─────────────────────────┤
│  Repository (ClienteRe) │  ← EntityManager.persist()
├─────────────────────────┤
│  Entity (Cliente)       │  ← @Entity, @Column
├─────────────────────────┤
│  PostgreSQL             │  ← INSERT, UPDATE, DELETE
└─────────────────────────┘
```

---

## 🔄 Relacionamentos JPA

### OneToMany (Cliente ↔ Servico)
```java
// Cliente.java
@OneToMany(mappedBy = "cliente")
private List<Servico> servicos;

// Servico.java
@ManyToOne
@JoinColumn(name = "cliente_id")
private Cliente cliente;
```

### ManyToMany (Servico ↔ Funcionario)
```java
// Servico.java
@ManyToMany
@JoinTable(name = "funcionario_servico", ...)
private List<Funcionario> funcionarios;

// Funcionario.java
@ManyToMany(mappedBy = "funcionarios")
private List<Servico> servicos;
```

---

## 📊 Estrutura do Banco

### Tabelas

```
clientes
├── id (PK)
├── nome_empresa
├── documento (UNIQUE)
├── endereco
├── telefone
├── email
└── status

funcionarios
├── id (PK)
├── nome
├── cpf (UNIQUE)
├── endereco
├── salario
├── cargo
└── status

servicos
├── id (PK)
├── nome_servico
├── descricao
├── data
├── valor
├── cliente_id (FK)
└── status

funcionario_servico
├── servico_id (PK, FK)
└── funcionario_id (PK, FK)

historico_servicos
├── id (PK)
├── servico_id (FK)
└── data_conclusao

agenda_servicos
├── id (PK)
├── servico_id (FK)
└── status_agenda
```

---

## 🐛 Erros Comuns

| Erro | Causa | Solução |
|------|-------|--------|
| "Connection refused" | PostgreSQL não rodando | Iniciar PostgreSQL |
| "Database detetizadora_master does not exist" | BD não criado | Executar CREATE DATABASE |
| "Table not found" | Flyway não migrou | `mvn clean install` |
| "Invalid password" | Credenciais erradas | Atualizar persistence.xml |
| "Invalid value for MonthOfYear" | Data inválida | Usar data correta |
| "Entrada vazia" | Campo deixado em branco | Digitar valor |

---

## 🔐 Segurança

### Arquivos a Ignorar (no .gitignore)
```
persistence.xml      # ⚠️ Credenciais!
.env                 # Variáveis
.idea/               # IDE
target/              # Compilados
*.class              # Classes
```

### Arquivos a Commitar
```
persistence.xml.example   # Template
src/                       # Código
pom.xml                    # Dependências
README.md                  # Documentação
.gitignore                 # Configuração
```

---

## 📈 Fluxo Típico de Operação

### Cadastrar Cliente

```
1. Menu: ClienteMenu.cadastrarCliente()
2. InputUtils: lerString() → nome
3. InputUtils: lerDocumento() → CPF/CNPJ validado
4. InputUtils: lerTelefone() → telefone validado
5. InputUtils: lerEmail() → email validado
6. Service: ClienteService.salvar()
   - Valida documento novamente
   - Persiste no BD
7. Menu: Exibe "Cliente cadastrado com ID X"
```

### Cadastrar Serviço

```
1. Menu: ServicoMenu.cadastrarServico()
2. InputUtils: lerString() → nome serviço
3. InputUtils: lerData() → data validada
4. InputUtils: lerValorServico() → valor > 0
5. Menu: Seleciona cliente (vinculação)
6. Service: ServicoService.salvar()
   - Valida cliente
   - Valida valor > 0
   - Persiste no BD
7. Menu: Exibe "Serviço cadastrado com ID X"
```

---

## 🧪 Testes Manuais Recomendados

1. **Validação de Documento**
   - Tentar: `123.456.789-10` → OK
   - Tentar: `123456789` → Erro
   - Tentar: `12.345.678/0001-95` → OK

2. **Validação de Data**
   - Tentar: `25/12/2026` → OK
   - Tentar: `12/42/5203` → Erro
   - Tentar: `30/02/2026` → Erro

3. **Validação de Valor**
   - Tentar: `100.50` → OK
   - Tentar: `0` → Erro
   - Tentar: `-50` → Erro

4. **CRUD Completo**
   - Cadastrar cliente
   - Listar clientes
   - Buscar por ID
   - Atualizar dados
   - Remover cliente

---

## 📞 Suporte

- 📖 README.md - Documentação completa
- ⚙️ CONFIGURACAO.md - Setup detalhado
- 🚀 README_SETUP.md - Quick start
- 🔍 Este arquivo - Quick reference

---

**Última atualização**: 10 de Abril de 2026

**Projeto**: Sistema de Detetização - Mensal 2
**Status**: ✅ CONCLUÍDO E ENTREGUE

