-- =====================
-- DADOS
-- =====================

INSERT INTO clientes (nome_empresa, documento, endereco, telefone, email, status) VALUES
    ('Empresa A', '123233210000102', 'Rua A, 123 - Foz do Iguacu', '999999999', 'aaaaaaaaaa@gmail.com', 'ATIVO'),
    ('Empresa B', '987654320000109', 'Rua B, 456 - Foz do Iguacu', '988888888', 'asdasdasda@hotmail.com', 'ATIVO');

INSERT INTO funcionarios (nome, cpf, telefone, email, salario, cargo, status) VALUES
    ('João Silva', '12345678900', '977777777', 'banana@gmail.com', 2500.00, 'FUNCIONARIO', 'ATIVO'),
    ('Maria Souza', '98765432100', '966666666', 'mamao@outlook.com', 3000.00, 'GERENTE', 'ATIVO');

INSERT INTO servicos (nome_servico, descricao, data, valor, status, cliente_id) VALUES
    ('Descupinização', 'Controle de cupins em madeira', '2025-04-11', 500.00, 'CONCLUIDO', 1),
    ('Dedetização comercial', 'Controle geral de pragas', '2025-04-15', 800.00, 'EM_ANDAMENTO', 2);

INSERT INTO agenda (data_agendada, hora, observacoes, status, servico_id, funcionario_id) VALUES
    ('2025-05-01', '14:00:00', 'Agendamento teste', 'AGENDADO', 1, 2),
    ('2025-05-02', '10:00:00', 'Agendamento teste 2', 'EM_ANDAMENTO', 2, 1);
