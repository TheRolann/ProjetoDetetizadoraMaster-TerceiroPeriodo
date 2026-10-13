-- Adiciona coluna senha em funcionarios
ALTER TABLE funcionarios ADD COLUMN IF NOT EXISTS senha varchar(100);

-- Senha padrao para funcionarios existentes (pode trocar)
UPDATE funcionarios SET senha = '1234' WHERE senha IS NULL;