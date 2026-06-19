-- =====================
-- AJUSTES PARA CONFORMIDADE COM JPA
-- =====================

-- Adiciona coluna endereco em funcionarioEntities se ainda não existir
ALTER TABLE funcionarioEntities ADD COLUMN IF NOT EXISTS endereco text;

-- Cria tabela funcionario_servico se não existir
CREATE TABLE IF NOT EXISTS funcionario_servico (
    servico_id integer NOT NULL REFERENCES servicoEntities(id) ON DELETE CASCADE,
    funcionario_id integer NOT NULL REFERENCES funcionarioEntities(id) ON DELETE CASCADE,
    PRIMARY KEY (servico_id, funcionario_id)
);

