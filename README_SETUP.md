# 🚀 SETUP DO PROJETO - Detetizadora Master

## ⚡ Pré-requisitos

- ✅ Java 21+
- ✅ Maven 3.9+ instalado e no PATH
- ✅ PostgreSQL 12+ rodando
- ✅ Banco de dados `detetizadora_master` criado

---

## 🏃 COMEÇAR RÁPIDO (5 minutos)

### 1️⃣ Clone o Repositório

```bash
git clone https://github.com/seu-usuario/ProjetoMensal-TerceiroPeriodo.git
cd ProjetoMensal-TerceiroPeriodo/Mensal1
```

### 2️⃣ Configure o Banco de Dados

**Crie o banco no PostgreSQL:**

```sql
CREATE DATABASE detetizadora_master;
```

**Copie o arquivo de configuração:**

```bash
# Windows (PowerShell)
Copy-Item src/main/resources/META-INF/persistence.xml.example `
          src/main/resources/META-INF/persistence.xml

# Linux/Mac
cp src/main/resources/META-INF/persistence.xml.example \
   src/main/resources/META-INF/persistence.xml
```

**Edite o arquivo `persistence.xml`:**

Abra `src/main/resources/META-INF/persistence.xml` e atualize:

```xml
<!-- Linha 16: URL do banco -->
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:postgresql://localhost:3006/detetizadora_master"/>

<!-- Linha 17: Usuário (padrão: postgres) -->
<property name="jakarta.persistence.jdbc.user" value="postgres"/>

<!-- Linha 18: Senha -->
<property name="jakarta.persistence.jdbc.password" value="asdwsad"/>
```

**Dica**: Se usou a porta padrão do PostgreSQL (5432), mude:
```xml
<property name="jakarta.persistence.jdbc.url" 
          value="jdbc:postgresql://localhost:5432/detetizadora_master"/>
```

### 3️⃣ Compile e Execute

```bash
# Instalar dependências e compilar
mvn clean install

# Executar a aplicação
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"
```

Se tudo der certo, o programa abrirá com o menu principal! 🎉

---

## ⚠️ SEGURANÇA - Leia com atenção!

- 🔒 O arquivo `persistence.xml` **está no `.gitignore`** - **NUNCA commit com suas credenciais**!
- 📋 `persistence.xml.example` é apenas um modelo - **NUNCA edite esse arquivo**!
- 👥 Cada pessoa configura seu próprio `persistence.xml` localmente
- 🔐 Suas credenciais ficam apenas na sua máquina

---

## 🐛 Troubleshooting Rápido

| Erro | Solução |
|------|---------|
| "Connection refused" | PostgreSQL não está rodando. Inicie-o! |
| "Database does not exist" | Execute: `CREATE DATABASE detetizadora_master;` |
| "Table not found" | Rode `mvn clean install` para executar as migrações |
| "Invalid password" | Verifique a senha em `persistence.xml` |

---

## 📚 Mais Detalhes?

- 📖 Ver `README.md` para documentação completa
- ⚙️ Ver `CONFIGURACAO.md` para instruções passo a passo
- 🎯 Ver arquivo `.gitignore` para arquivos ignorados

---

## ✅ Checklist

- [ ] Java 21+ instalado
- [ ] Maven instalado
- [ ] PostgreSQL rodando
- [ ] Banco `detetizadora_master` criado
- [ ] `persistence.xml` copiado do `.example`
- [ ] Credenciais corretas no `persistence.xml`
- [ ] `mvn clean install` executado com sucesso
- [ ] Aplicação rodando sem erros

---

**Última atualização**: 10 de Abril de 2026

**Dúvidas? Consulte a documentação ou o README.md!** 💬
