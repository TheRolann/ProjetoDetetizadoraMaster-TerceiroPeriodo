# 🔧 Configuração do Ambiente Local

## ⚠️ IMPORTANTE: Banco de Dados

O arquivo `persistence.xml` **NÃO é commitado** no repositório por questões de segurança (contém credenciais do banco).

### Passo 1: Copiar o arquivo de exemplo

```bash
cp src/main/resources/META-INF/persistence.xml.example src/main/resources/META-INF/persistence.xml
```

### Passo 2: Editar o `persistence.xml` com suas credenciais locais

Abra o arquivo `persistence.xml` e atualize:

```xml
<!-- URL de conexao ao banco de dados -->
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:PORTA/detetizadora_master"/>

<!-- Usuario do banco -->
<property name="jakarta.persistence.jdbc.user" value="seu_usuario"/>

<!-- Senha do banco -->
<property name="jakarta.persistence.jdbc.password" value="sua_senha"/>
```

**Exemplo com dados locais:**
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/detetizadora_master"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="minhasenha123"/>
```

### Passo 3: Verificar banco de dados

Certifique-se de que:
1. ✅ PostgreSQL está rodando na sua máquina
2. ✅ Banco de dados `detetizadora_master` foi criado
3. ✅ Usuário e senha estão corretos

### Passo 4: Rodar a aplicação

```bash
mvn clean compile
java -cp target/classes:target/lib/* br.edu.uniamerica.projetomensal.Main
```

## 📝 Notas

- ⛔ **NUNCA commite o `persistence.xml` com credenciais reais**
- ✅ O arquivo `persistence.xml.example` fica no repositório como modelo
- 🔒 Cada desenvolvedora cria seu próprio `persistence.xml` localmente
- 📌 O `persistence.xml` está no `.gitignore` para proteção

## 🐘 Configuração PostgreSQL (First Time)

Se for a primeira vez rodando:

```sql
-- No pgAdmin ou psql
CREATE DATABASE detetizadora_master;

-- Usuário padrão é 'postgres'
-- Senha: conforme sua instalação
```

Depois é só copiar o `persistence.xml.example` e atualizar as credenciais! 🚀

