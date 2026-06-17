# 🦟 Sistema de Gestão de Serviços — Detetizadora Master

Sistema desktop desenvolvido em Java para gestão de clientes, funcionários e serviços de uma empresa de controle de pragas, substituindo o uso informal do WhatsApp por uma solução estruturada.

Projeto acadêmico desenvolvido no curso de Análise e Desenvolvimento de Sistemas — UniAmérica Descomplica, Foz do Iguaçu — PR.

---

## 👥 Time

| Nome                             |
|----------------------------------|
| Luan Augusto de Castro Rolan     |
| Adriano Ramos dos Santos Junior  |
| Victor Hugo de Almeida Marques   |
| Victor Luís da Luz               |

**Mentor:** Riad Younes — Engenheiro de Software FrontEnd  
**Orientador:** Victor Matheus de Souza Muller

---

## 🛠 Tecnologias

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 21 | Linguagem principal |
| PostgreSQL | 12+ | Banco de dados relacional |
| Hibernate | 6.4.4 | Implementação JPA |
| Flyway | 10.10.0 | Versionamento de banco |
| Maven | 3.9+ | Gerenciador de dependências |
| Swing | — | Interface gráfica |

---

## 🏗 Arquitetura

O sistema segue uma arquitetura em camadas inspirada em MVC, com Service e Repository separados:

```
View (Swing)
   ↓
Controller
   ↓
Service     → regras de negócio e transações (begin / commit / rollback)
   ↓
Repository  → único ponto que fala com o EntityManager (JPA/Hibernate)
   ↓
PostgreSQL
```

| Camada | Pacote | Responsabilidade |
|---|---|---|
| View | `view`, `view.panels` | Telas Swing (`LoginFrame`, `MainFrame`, painéis). Não acessa Service nem banco diretamente. |
| Controller | `controller` | Recebe as ações da View e delega ao Service correspondente. Não importa nada de `javax.swing`. |
| Service | `service` | Regras de negócio, validações (`NegocioException`) e controle de transação. |
| Repository | `repository` | Persistência via JPA — único lugar que usa o `EntityManager`. |
| Model | `model`, `model.enums` | Entidades JPA (`Cliente`, `Funcionario`, `Servico`) e enums (`Status`, `Cargo`). |

Toda ação da interface passa pelo Controller — a View nunca chama Service ou Repository diretamente.

---

## ✅ Pré-requisitos

- Java JDK 21+
- Maven 3.9+
- PostgreSQL 12+ rodando localmente

Verificar instalação:

```bash
java -version
mvn -version
psql --version
```

---

## 🚀 Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/TheRolann/ProjetoMensal-TerceiroPeriodo.git
cd ProjetoMensal-TerceiroPeriodo/Mensal1
```

### 2. Criar o banco de dados

No PostgreSQL, execute:

```sql
CREATE DATABASE detetizadora_master;
```

### 3. Configurar o arquivo de persistência

```bash
# Windows (PowerShell)
Copy-Item src/main/resources/META-INF/persistence.xml.example `
          src/main/resources/META-INF/persistence.xml

# Linux / Mac
cp src/main/resources/META-INF/persistence.xml.example \
   src/main/resources/META-INF/persistence.xml
```

Edite o arquivo copiado com suas credenciais:

```xml
<property name="jakarta.persistence.jdbc.url"
          value="jdbc:postgresql://localhost:5432/detetizadora_master"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="sua_senha"/>
```

### 4. Configurar variáveis de ambiente para o Flyway

O Flyway lê as credenciais via variáveis de ambiente. Configure antes de rodar:

**Windows (PowerShell):**
```powershell
$env:DB_URL      = "jdbc:postgresql://localhost:5432/detetizadora_master"
$env:DB_USER     = "postgres"
$env:DB_PASSWORD = "sua_senha"
```

**Linux / Mac:**
```bash
export DB_URL="jdbc:postgresql://localhost:5432/detetizadora_master"
export DB_USER="postgres"
export DB_PASSWORD="sua_senha"
```

### 5. Compilar e executar

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"
```

Ou via JAR:

```bash
mvn clean package
java -jar target/Mensal1-1.0-SNAPSHOT.jar
```

---

## 🔐 Login

O Flyway cria automaticamente os dados iniciais. Para acessar o sistema use:

| Campo | Valor |
|-------|-------|
| Nome | João Silva |
| Senha | 1234 |

Para cadastrar novos usuários, acesse a aba **Funcionários** após o login e defina a senha diretamente no cadastro.

---

## ✨ Funcionalidades

### Clientes
- Cadastrar, editar, listar e excluir clientes
- Validação de CPF (11 dígitos) e CNPJ (14 dígitos)
- Validação de e-mail e telefone
- Controle de status (Ativo / Inativo)

### Funcionários
- Cadastrar, editar, listar e excluir funcionários
- Controle de cargo (Gerente / Funcionário)
- Controle de salário
- Login com autenticação por nome e senha
- Exclusão lógica (muda status para Inativo)

### Serviços
- Cadastrar, editar, listar e excluir serviços
- Vinculação com cliente
- Controle de status (Agendado / Em andamento / Concluído / Inativo)
- Validação de data e valor

### Relatórios
- **Clientes** — total, ativos e inativos
- **Funcionários** — total por cargo, soma de salários
- **Serviços** — faturamento, pendentes, serviço mais lucrativo
- **Agenda** — serviços dos próximos 3 meses com valor previsto

---

## 🗄 Banco de dados

### Tabelas
- `clientes` — dados dos clientes
- `funcionarios` — dados dos funcionários com senha
- `servicos` — serviços vinculados a clientes
- `agenda` — agendamentos vinculados a serviços
- `historico_servicos` — log automático de serviços concluídos
- `funcionario_servico` — relacionamento ManyToMany

### Triggers
| Trigger | Ação |
|---------|------|
| `trg_historico_servico` | Registra automaticamente serviços concluídos |
| `trg_validar_status_funcionario` | Bloqueia cadastro de funcionário inativo |
| `trg_atualizar_agenda_servico` | Atualiza agenda ao concluir serviço |

---

## 🐛 Troubleshooting

| Erro | Causa | Solução |
|------|-------|---------|
| `Connection refused` | PostgreSQL não está rodando | Inicie o PostgreSQL |
| `Database does not exist` | Banco não criado | Execute `CREATE DATABASE detetizadora_master` |
| `Table not found` | Flyway não rodou | Execute `mvn clean install` |
| `Invalid password` | Credenciais erradas | Verifique `persistence.xml` e variáveis de ambiente |
| `Não é possível excluir` | Chave estrangeira | Exclua os registros dependentes primeiro |

---

