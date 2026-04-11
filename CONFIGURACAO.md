# 🔧 Configuração do Ambiente Local

## ⚠️ IMPORTANTE: Banco de Dados

O arquivo `persistence.xml` **NÃO é commitado** no repositório por questões de segurança (contém credenciais do banco).

---

## 📋 Pré-requisitos

Antes de começar, verifique se tem instalado:

```bash
# Java 21+
java -version

# Maven 3.9+
mvn -version

# PostgreSQL 12+
psql --version
```

Se não tiver PostgreSQL, baixe em: https://www.postgresql.org/download/

---

## 🚀 Configuração Passo a Passo

### Passo 1: Criar Banco de Dados no PostgreSQL

No **pgAdmin** ou via **psql**, execute:

```sql
-- Criar usuário (se não existir)
CREATE USER postgres WITH PASSWORD 'asdwsad';

-- Criar banco de dados
CREATE DATABASE detetizadora_master OWNER postgres;

-- Dar privilégios
GRANT ALL PRIVILEGES ON DATABASE detetizadora_master TO postgres;

-- Verificar conexão
\c detetizadora_master
```

**Ou pela linha de comando:**

```bash
# Conectar ao PostgreSQL
psql -U postgres

# Executar os comandos SQL acima
```

---

### Passo 2: Copiar Arquivo de Configuração

```bash
# No diretório Mensal1/
cp src/main/resources/META-INF/persistence.xml.example \
   src/main/resources/META-INF/persistence.xml
```

---

### Passo 3: Editar `persistence.xml` com suas Credenciais

Abra o arquivo `src/main/resources/META-INF/persistence.xml` e atualize as linhas:

```xml
<!-- URL de conexão ao banco de dados (TROCAR PORTA se necessário) -->
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:postgresql://localhost:3006/detetizadora_master"/>

<!-- Usuário do banco (padrão: postgres) -->
<property name="jakarta.persistence.jdbc.user" value="postgres"/>

<!-- Senha do banco (TROCAR com sua senha) -->
<property name="jakarta.persistence.jdbc.password" value="asdwsad"/>
```

**IMPORTANTE**: 
- ⚠️ Trocar `localhost:3006` pela porta correta do seu PostgreSQL
- ⚠️ Trocar `asdwsad` pela senha que você cadastrou

**Exemplo com porta padrão (5432):**
```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:postgresql://localhost:5432/detetizadora_master"/>
```

---

### Passo 4: Atualizar FlywayConfig.java (Opcional)

Se usou porta diferente, também atualize em `src/main/java/br/edu/uniamerica/projetomensal/config/FlywayConfig.java`:

```java
public class FlywayConfig {
    public static void migrar() {
        Flyway flyway = Flyway.configure()
            // ⚠️ TROCAR AQUI TAMBÉM se porta for diferente
            .dataSource("jdbc:postgresql://localhost:3006/detetizadora_master", 
                        "postgres", "asdwsad")
            .validateOnMigrate(false)
            .outOfOrder(true)
            .load();
        flyway.repair();
        flyway.migrate();
    }
}
```

---

### Passo 5: Instalar Dependências e Compilar

```bash
# Dentro do diretório Mensal1/
mvn clean install
```

Isso irá:
- ✅ Baixar dependências do Maven
- ✅ Compilar o código Java
- ✅ Executar Flyway (criar tabelas automaticamente)
- ✅ Gerar arquivo JAR

**Se tudo der certo, você verá:**
```
[INFO] BUILD SUCCESS
```

---

## ▶️ Executar a Aplicação

### Opção 1: Via IntelliJ IDEA (Recomendado)

1. Abrir o projeto em IntelliJ
2. Navegar até `Main.java`
3. Clicar com botão direito → "Run 'Main.main()'"

### Opção 2: Via Maven

```bash
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"
```

### Opção 3: Via JAR

```bash
java -jar target/Mensal1-1.0-SNAPSHOT.jar
```

---

## 🐛 Troubleshooting

### ❌ "Connection refused"

```
Problema: PostgreSQL não está rodando

Solução:
1. Verificar se PostgreSQL está rodando
   - Windows: Services → PostgreSQL
   - Linux: sudo systemctl status postgresql
   - Mac: brew services list

2. Iniciar PostgreSQL
   - Windows: Services → PostgreSQL → Start
   - Linux: sudo systemctl start postgresql
   - Mac: brew services start postgresql

3. Testar conexão
   psql -U postgres -c "SELECT version();"
```

### ❌ "Table not found"

```
Problema: Flyway não executou as migrações

Solução:
1. Limpar e reinstalar
   mvn clean install

2. Verificar se banco existe
   psql -U postgres -l

3. Se não existir, criar
   CREATE DATABASE detetizadora_master;
```

### ❌ "Authentication failed"

```
Problema: Credenciais incorretas em persistence.xml

Solução:
1. Verificar usuário e senha
   psql -U postgres -c "SELECT current_user;"

2. Atualizar persistence.xml com credenciais corretas

3. Limpar cache
   mvn clean install
```

### ❌ "Invalid value for MonthOfYear"

```
Problema: Data inválida (ex: 12/42/5203)

Solução:
- InputUtils.lerData() agora valida datas
- Digitar data correta no formato DD/MM/AAAA
- Exemplo válido: 25/12/2026
```

---

## 📝 Notas Importantes

- ⛔ **NUNCA commite o `persistence.xml` com credenciais reais**
- ✅ O arquivo `persistence.xml.example` fica no repositório como modelo
- 🔒 Cada desenvolvedor cria seu próprio `persistence.xml` localmente
- 📌 O `persistence.xml` está no `.gitignore` para proteção
- 🔐 As credenciais são locais e específicas de cada máquina

---

## 🔄 Próximos Passos

Depois de configurar:

1. ✅ Verificar se tabelas foram criadas
   ```sql
   \dt  -- Lista todas as tabelas
   ```

2. ✅ Testar CRUD básico
   - Abrir aplicação
   - Cadastrar um cliente
   - Listar clientes

3. ✅ Se tudo funcionar, você está pronto!

---

## 💡 Dicas

- **Porta PostgreSQL**: Se ainda usa a padrão (5432), mude em `persistence.xml`
- **Dados de Teste**: Use dados fictícios para não poluir o banco
- **Backup**: Faça backup do banco antes de testes importantes
- **Logs**: Verifique logs se algo der errado: `mvn clean install -X`

---

**Última atualização**: 10 de Abril de 2026
