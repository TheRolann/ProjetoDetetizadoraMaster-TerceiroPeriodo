-- =====================
-- DADOS
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
