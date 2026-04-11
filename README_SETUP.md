# 🚀 SETUP DO PROJETO - Detetizadora Master

## ⚡ Pré-requisitos

- ✅ Java 21+
- ✅ Maven instalado e no PATH
- ✅ PostgreSQL rodando
- ✅ Banco de dados `detetizadora_master` criado

## 🏃 COMEÇAR RÁPIDO (5 minutos)

### 1️⃣ Clone ou Pull do repositório
```bash
git clone https://github.com/seu-usuario/ProjetoMensal-TerceiroPeriodo.git
cd ProjetoMensal-TerceiroPeriodo
```

### 2️⃣ Configure o banco de dados local

**Copie o arquivo de exemplo:**
```bash
# Windows (PowerShell)
Copy-Item Mensal1/src/main/resources/META-INF/persistence.xml.example Mensal1/src/main/resources/META-INF/persistence.xml

# Linux/Mac
cp Mensal1/src/main/resources/META-INF/persistence.xml.example Mensal1/src/main/resources/META-INF/persistence.xml
```

**Edite o arquivo `persistence.xml`:**
- Abra em um editor de texto
- Procure por `jdbc:postgresql://localhost:PORTA/detetizadora_master`
- Substitua `PORTA` pela sua porta PostgreSQL (padrão: `5432`)
- Substitua `user` e `password` pelas suas credenciais do PostgreSQL

Exemplo:
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/detetizadora_master"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="sua_senha_aqui"/>
```

### 3️⃣ Compile e rode

```bash
# Entra na pasta
cd Mensal1

# Limpa e compila
mvn clean compile

# Roda o projeto
mvn exec:java -Dexec.mainClass="br.edu.uniamerica.projetomensal.Main"
```

## ⚠️ IMPORTANTE - Segurança

- 🔒 O arquivo `persistence.xml` **está no `.gitignore`** - nunca commit com suas credenciais
- 📋 `persistence.xml.example` é o modelo - nunca edite esse arquivo!
- 👥 Cada pessoa configura seu próprio `persistence.xml` localmente

## 📚 Mais detalhes?

Veja o arquivo `CONFIGURACAO.md` para instruções completas!

## ❓ Problemas?

**"Arquivo persistence.xml não encontrado"**
→ Você precisa rodar o comando do passo 2️⃣

**"Connection refused"**
→ Verifique se PostgreSQL está rodando e se a porta/credenciais estão corretas

**"Banco de dados não existe"**
→ Crie o banco: `CREATE DATABASE detetizadora_master;`

---

**Dúvidas? Chama no grupo!** 💬

