CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- So re-hash quem ainda esta em texto puro (idempotente, nao quebra se rodar 2x)
UPDATE funcionarios
SET senha = crypt(senha, gen_salt('bf', 10))
WHERE senha IS NOT NULL
  AND senha !~ '^\$2[aby]\$';