-- Adiciona coluna senha em funcionarioEntities
ALTER TABLE funcionarioEntities ADD COLUMN IF NOT EXISTS senha varchar(100);

-- Senha padrao para funcionarioEntities existentes (pode trocar)
UPDATE funcionarioEntities SET senha = '1234' WHERE senha IS NULL;