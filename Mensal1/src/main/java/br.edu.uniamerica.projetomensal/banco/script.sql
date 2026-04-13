-- LOGICA DO BANCO DE DADOS PARA PROJETO DE CONTROLE DE SERVICOS DE DEDETIZACAO
-- ESTE SCRIPT NAO E UTILIZADO DIRETAMENTE NO PROJETO, MAS SIM COMO REFERENCIA DA ESTRUTURA DO BANCO

-- =====================
-- CRUD - CREATE
-- =====================

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
-- DADOS DE TESTE
-- =====================

INSERT INTO clientes (nome_empresa, documento, endereco, telefone, email, status) VALUES
    ('Empresa A', '123233210000102', 'Rua A, 123 - Foz do Iguacu', '(45) 99999-9999', 'aaaaaaaaaa@gmail.com', 'ATIVO'),
    ('Empresa B', '987654320000109', 'Rua B, 456 - Foz do Iguacu', '(45) 98888-8888', 'asdasdasda@hotmail.com', 'INATIVO');

INSERT INTO funcionarios (nome, cpf, telefone, email, salario, cargo, status) VALUES
    ('João Silva', '123.456.789-00', '(45) 97777-7777', 'banana@gmail.com', 2500.00, 'FUNCIONARIO', 'INATIVO'),
    ('Maria Souza', '987.654.321-00', '(45) 96666-6666', 'mamao@outlook.com', 3000.00, 'GERENTE', 'ATIVO');

INSERT INTO servicos (nome_servico, descricao, data, valor, status, cliente_id) VALUES
    ('Descupinização', 'Controle de cupins em madeira', '2025-04-11', 500.00, 'CONCLUIDO', 1),
    ('Dedetização comercial', 'Controle geral de pragas', '2025-04-15', 800.00, 'EM_ANDAMENTO', 2);

INSERT INTO agenda (data_agendada, hora, observacoes, status, servico_id, funcionario_id) VALUES
    ('2025-05-01', '14:00:00', 'Agendamento teste', 'AGENDADO', 1, 2),
    ('2025-05-02', '10:00:00', 'Agendamento teste 2', 'EM_ANDAMENTO', 2, 1);

-- Listar as tabelas criadas
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;


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
SELECT * FROM clientes WHERE status = 'INATIVO';
SELECT * FROM funcionarios WHERE cargo = 'GERENTE';
SELECT * FROM servicos WHERE status = 'EM_ANDAMENTO';

-- =====================
-- CRUD - UPDATE
-- =====================

UPDATE clientes
SET status = 'INATIVO'
WHERE id = 1;

UPDATE funcionarios
SET salario = 3600.00
WHERE id = 1;

UPDATE servicos
SET status = 'CONCLUIDO'
WHERE id = 2;

UPDATE agenda
SET funcionario_id = 2, status = 'CONCLUIDO'
WHERE id = 1;

-- =====================
-- CRUD - DELETE
-- =====================

-- Sempre apaga filho antes do pai por conta das FK
DELETE FROM agenda WHERE id = 2;
DELETE FROM servicos WHERE id = 2;
DELETE FROM funcionarios WHERE id = 1;


-- =====================
-- JOINs
-- =====================

-- INNER JOIN - Servicos com cliente cadastrado
SELECT s.id, s.nome_servico, s.status, c.nome_empresa AS cliente
FROM servicos s
INNER JOIN clientes c ON s.cliente_id = c.id;

-- LEFT JOIN - Todos os servicos mesmo sem funcionarios
SELECT s.nome_servico, s.status, a.data_agendada, f.nome AS funcionario
FROM servicos s
LEFT JOIN agenda a ON a.servico_id = s.id
LEFT JOIN funcionarios f ON a.funcionario_id = f.id;

-- RIGHT JOIN - Todos os funcionarios mesmo sem agendamento
SELECT f.nome AS funcionario, f.cargo, a.data_agendada, a.status AS status_agenda
FROM agenda a
RIGHT JOIN funcionarios f ON a.funcionario_id = f.id;

-- FULL - tudo de clientes e servicos mesmo sem correspondencia
SELECT c.nome_empresa AS cliente, c.status AS status_cliente, s.nome_servico, s.status AS status_servico
FROM clientes c
FULL JOIN servicos s ON s.cliente_id = c.id;


-- =====================
-- TRIGGERS E FUNCOES
-- =====================

-- Tabela de historico
CREATE TABLE IF NOT EXISTS historico_servicos (
    id SERIAL PRIMARY KEY,
    servico_id INTEGER NOT NULL REFERENCES servicos(id),
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT
);

-- Trigger para Registar Historico
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_registrar_historico_servico()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'CONCLUIDO' AND OLD.status != 'CONCLUIDO' THEN
        INSERT INTO historico_servicos (servico_id, observacao)
        VALUES (NEW.id, 'Servico concluido automaticamente por trigger');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_historico_servico ON servicos;
CREATE TRIGGER trg_historico_servico
AFTER UPDATE ON servicos
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historico_servico();

-- Trigger para validar Status do Funcionario
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_validar_status_funcionario()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'INATIVO' THEN
        RAISE EXCEPTION 'Nao e permitido cadastrar funcionario INATIVO';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_validar_status_funcionario ON funcionarios;
CREATE TRIGGER trg_validar_status_funcionario
BEFORE INSERT ON funcionarios
FOR EACH ROW
EXECUTE FUNCTION fn_validar_status_funcionario();

-- Trigger para atualizar agenda quando servico finalizado
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_atualizar_agenda_servico()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'CONCLUIDO' AND OLD.status != 'CONCLUIDO' THEN
        UPDATE agenda
        SET status = 'CONCLUIDO'
        WHERE servico_id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_atualizar_agenda_servico ON servicos;
CREATE TRIGGER trg_atualizar_agenda_servico
AFTER UPDATE ON servicos
FOR EACH ROW
EXECUTE FUNCTION fn_atualizar_agenda_servico();

-- =====================
-- TESTES DOS TRIGGERS
-- =====================

-- Teste 1 e 3: Mudar status do servico para CONCLUIDO
-- Isso vai disparar Trigger 1 (historico) e Trigger 3 (atualizar agenda)
UPDATE servicos
SET status = 'CONCLUIDO'
WHERE id = 2;

-- Verificar historico registrado (Trigger 1)
SELECT * FROM historico_servicos;

-- Verificar agenda atualizada (Trigger 3)
SELECT * FROM agenda WHERE servico_id = 2;

-- Teste 2: Tentar inserir funcionario com status INATIVO
-- Deve gerar erro (Trigger 2)
INSERT INTO funcionarios (nome, cpf, telefone, email, salario, cargo, status)
VALUES ('Teste Inativo', '111.111.111-11', '(45) 90000-0000', 'teste@email.com', 1000.00, 'FUNCIONARIO', 'INATIVO');
