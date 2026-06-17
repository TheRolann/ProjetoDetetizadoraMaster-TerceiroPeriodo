-- =====================
-- CREATE
-- =====================

-- Cliente
CREATE TABLE IF NOT EXISTS clienteEntities (
    id serial PRIMARY KEY,
    nome_empresa varchar(150) NOT NULL,
    documento varchar(18) NOT NULL UNIQUE,
    endereco text,
    telefone varchar(20),
    email varchar(100),
    status varchar(20) NOT NULL DEFAULT 'ATIVO'
);

-- Funcionarios
CREATE TABLE IF NOT EXISTS funcionarioEntities (
    id serial PRIMARY KEY,
    nome varchar(150) NOT NULL,
    cpf varchar(14) NOT NULL UNIQUE,
    telefone varchar(20),
    email varchar(100),
    endereco text,
    salario numeric(10,2),
    cargo varchar(20) NOT NULL DEFAULT 'FUNCIONARIO',
    status varchar(20) NOT NULL DEFAULT 'ATIVO'
);

-- Servicos
CREATE TABLE IF NOT EXISTS servicoEntities (
    id serial PRIMARY KEY,
    nome_servico varchar(150) NOT NULL,
    descricao text,
    data date NOT NULL,
    valor numeric(10,2),
    status varchar(20) NOT NULL DEFAULT 'AGENDADO',
    cliente_id integer NOT NULL REFERENCES clienteEntities(id)
);

-- Agenda
CREATE TABLE IF NOT EXISTS agenda (
    id serial PRIMARY KEY,
    data_agendada date NOT NULL,
    hora time NOT NULL,
    observacoes text,
    status varchar(20) NOT NULL DEFAULT 'AGENDADO',
    servico_id integer NOT NULL REFERENCES servicoEntities(id),
    funcionario_id integer REFERENCES funcionarioEntities(id)
);

-- Tabela de historico
CREATE TABLE IF NOT EXISTS historico_servicos (
    id SERIAL PRIMARY KEY,
    servico_id INTEGER NOT NULL REFERENCES servicoEntities(id),
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT
);

-- Tabela de relacionamento ManyToMany entre Funcionario e Servico
CREATE TABLE IF NOT EXISTS funcionario_servico (
    servico_id integer NOT NULL REFERENCES servicoEntities(id),
    funcionario_id integer NOT NULL REFERENCES funcionarioEntities(id),
    PRIMARY KEY (servico_id, funcionario_id)
);
