-- =====================
-- CONSULTAS COM JOINs
-- =====================

-- INNER JOIN: Servicos com cliente (apenas registros que existem em ambas as tabelas)
-- Mostra: ID do servico, nome, status e cliente relacionado
SELECT
    s.id AS servico_id,
    s.nome_servico,
    s.status,
    s.valor,
    c.nome_empresa AS cliente
FROM servicos s
INNER JOIN clientes c ON s.cliente_id = c.id;

-- LEFT JOIN: Todos os servicos + agenda + funcionaio (mesmo sem agendamento)
-- Mostra: Servico, data agendada e funcionario responsavel (pode ter NULL)
SELECT
    s.nome_servico,
    s.status AS status_servico,
    a.data_agendada,
    a.status AS status_agenda,
    f.nome AS funcionario_responsavel
FROM servicos s
LEFT JOIN agenda a ON a.servico_id = s.id
LEFT JOIN funcionarios f ON a.funcionario_id = f.id;

-- RIGHT JOIN: Todos os funcionarios + agenda (mesmo sem agendamento)
-- Mostra: Funcionario, cargo e dados do agendamento (pode ter NULL)
SELECT
    f.nome AS funcionario,
    f.cargo,
    f.status,
    a.data_agendada,
    a.status AS status_agenda,
    s.nome_servico
FROM agenda a
RIGHT JOIN funcionarios f ON a.funcionario_id = f.id
LEFT JOIN servicos s ON a.servico_id = s.id;

-- FULL OUTER JOIN: Todos os clientes + todos os servicos (com ou sem correspondencia)
-- Mostra: Cliente e servico, preenchendo com NULL onde nao houver correspondencia
SELECT
    c.nome_empresa AS cliente,
    c.status AS status_cliente,
    s.nome_servico,
    s.status AS status_servico,
    s.valor
FROM clientes c
FULL OUTER JOIN servicos s ON s.cliente_id = c.id;

