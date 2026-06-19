-- =====================
-- CONSULTAS COM JOINs
-- =====================

-- INNER JOIN: Servicos com clienteEntity (apenas registros que existem em ambas as tabelas)
-- Mostra: ID do servico, nome, status e clienteEntity relacionado
SELECT
    s.id AS servico_id,
    s.nome_servico,
    s.status,
    s.valor,
    c.nome_empresa AS clienteEntity
FROM servicoEntities s
INNER JOIN clienteEntities c ON s.cliente_id = c.id;

-- LEFT JOIN: Todos os servicoEntities + agenda + funcionaio (mesmo sem agendamento)
-- Mostra: Servico, data agendada e funcionarioEntity responsavel (pode ter NULL)
SELECT
    s.nome_servico,
    s.status AS status_servico,
    a.data_agendada,
    a.status AS status_agenda,
    f.nome AS funcionario_responsavel
FROM servicoEntities s
LEFT JOIN agenda a ON a.servico_id = s.id
LEFT JOIN funcionarioEntities f ON a.funcionario_id = f.id;

-- RIGHT JOIN: Todos os funcionarioEntities + agenda (mesmo sem agendamento)
-- Mostra: Funcionario, cargo e dados do agendamento (pode ter NULL)
SELECT
    f.nome AS funcionarioEntity,
    f.cargo,
    f.status,
    a.data_agendada,
    a.status AS status_agenda,
    s.nome_servico
FROM agenda a
RIGHT JOIN funcionarioEntities f ON a.funcionario_id = f.id
LEFT JOIN servicoEntities s ON a.servico_id = s.id;

-- FULL OUTER JOIN: Todos os clienteEntities + todos os servicoEntities (com ou sem correspondencia)
-- Mostra: Cliente e servico, preenchendo com NULL onde nao houver correspondencia
SELECT
    c.nome_empresa AS clienteEntity,
    c.status AS status_cliente,
    s.nome_servico,
    s.status AS status_servico,
    s.valor
FROM clienteEntities c
FULL OUTER JOIN servicoEntities s ON s.cliente_id = c.id;

