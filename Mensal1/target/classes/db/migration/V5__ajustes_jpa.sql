-- =====================
-- AJUSTES PARA CONFORMIDADE COM JPA
-- =====================

-- Adiciona coluna endereco em funcionarios se ainda não existir
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS endereco text;

-- Cria tabela funcionario_servico se não existir
CREATE TABLE IF NOT EXISTS funcionario_servico (
    servico_id integer NOT NULL REFERENCES servicos(id) ON DELETE CASCADE,
    funcionario_id integer NOT NULL REFERENCES funcionarios(id) ON DELETE CASCADE,
    PRIMARY KEY (servico_id, funcionario_id)
);

