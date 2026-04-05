-- Cliente
CREATE TABLE IF NOT EXISTS clientes (
    id serial PRIMARY KEY,
    nome_empresa varchar(150) NOT NULL,
    documento varchar(18) NOT NULL UNIQUE,
    endereco text,
    telefone varchar(20),
    email varchar(100),
    status varchar(20) NOT NULL DEFAULT 'ATIVO'
    );

-- Funcionarios
CREATE TABLE IF NOT EXISTS funcionarios (
    id serial PRIMARY KEY,
    nome varchar(150) NOT NULL,
    cpf varchar(14) NOT NULL UNIQUE,
    telefone varchar(20),
    email varchar(100),
    salario numeric(10,2),
    cargo varchar(20) NOT NULL DEFAULT 'FUNCIONARIO',
    status varchar(20) NOT NULL DEFAULT 'ATIVO'
    );

-- Servicos
CREATE TABLE IF NOT EXISTS servicos (
    id serial PRIMARY KEY,
    nome_servico varchar(150) NOT NULL,
    descricao text,
    data date NOT NULL,
    valor numeric(10,2),
    status varchar(20) NOT NULL DEFAULT 'AGENDADO',
    cliente_id integer NOT NULL REFERENCES clientes(id)
    );

-- Agenda
CREATE TABLE IF NOT EXISTS agenda (
      id serial PRIMARY KEY,
      data_agendada date NOT NULL,
      hora time NOT NULL,
      observacoes text,
    status varchar(20) NOT NULL DEFAULT 'AGENDADO',
    servico_id integer NOT NULL REFERENCES servicos(id),
    funcionario_id integer REFERENCES funcionarios(id)
);

-- =====================
-- CRUD - SELECT
-- =====================

-- Listar todos
SELECT * FROM clientes;
SELECT * FROM funcionarios;
SELECT * FROM servicos;
SELECT * FROM agenda;

-- Buscar por ID
SELECT * FROM clientes WHERE id = 1;
SELECT * FROM funcionarios WHERE id = 1;
SELECT * FROM servicos WHERE id = 1;
SELECT * FROM agenda WHERE id = 1;

-- Buscar por status
SELECT * FROM clientes WHERE status = 'ATIVO';
SELECT * FROM funcionarios WHERE cargo = 'GERENTE';
SELECT * FROM servicos WHERE status = 'AGENDADO';

-- =====================
-- CRUD - UPDATE
-- =====================

UPDATE clientes
SET status = 'INATIVO'
WHERE id = ;