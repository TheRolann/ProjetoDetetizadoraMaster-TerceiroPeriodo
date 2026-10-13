-- =====================
-- DADOS
-- =====================

INSERT INTO clientes (nome_empresa, documento, endereco, telefone, email, status) VALUES
    ('Empresa A', '11222333000181', 'Rua A, 123 - Foz do Iguacu', '45999999999', 'empresa.a@example.test', 'ATIVO'),
    ('Empresa B', '11444777000161', 'Rua B, 456 - Foz do Iguacu', '45988888888', 'empresa.b@example.test', 'ATIVO');

INSERT INTO funcionarios (nome, cpf, telefone, email, salario, cargo, status) VALUES
    ('João Silva', '52998224725', '45977777777', 'joao@example.test', 2500.00, 'FUNCIONARIO', 'ATIVO'),
    ('Maria Souza', '11144477735', '45966666666', 'maria@example.test', 3000.00, 'GERENTE', 'ATIVO');

INSERT INTO servicos (nome_servico, descricao, data, valor, status, cliente_id) VALUES
    ('Descupinização', 'Controle de cupins em madeira', '2025-04-11', 500.00, 'CONCLUIDO', 1),
    ('Dedetização comercial', 'Controle geral de pragas', '2025-04-15', 800.00, 'EM_ANDAMENTO', 2);

INSERT INTO agenda (data_agendada, hora, observacoes, status, servico_id, funcionario_id) VALUES
    ('2025-05-01', '14:00:00', 'Agendamento teste', 'AGENDADO', 1, 2),
    ('2025-05-02', '10:00:00', 'Agendamento teste 2', 'EM_ANDAMENTO', 2, 1);
