-- Alinha agendamentos antigos com serviços que já foram encerrados.
UPDATE agenda a SET status = s.status
FROM servicos s WHERE a.servico_id = s.id
AND s.status IN ('CONCLUIDO', 'INATIVO')
AND a.status IN ('AGENDADO', 'EM_ANDAMENTO');

CREATE OR REPLACE FUNCTION fn_registrar_historico_servico()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'CONCLUIDO' THEN
        IF TG_OP = 'INSERT' THEN
            INSERT INTO historico_servicos (servico_id, observacao)
            VALUES (NEW.id, 'Serviço cadastrado como concluído');
        ELSIF OLD.status IS DISTINCT FROM NEW.status THEN
            INSERT INTO historico_servicos (servico_id, observacao)
            VALUES (NEW.id, 'Serviço concluído');
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER trg_historico_servico ON servicos;
CREATE TRIGGER trg_historico_servico AFTER INSERT OR UPDATE ON servicos
FOR EACH ROW EXECUTE FUNCTION fn_registrar_historico_servico();

INSERT INTO historico_servicos (servico_id, observacao)
SELECT s.id, 'Serviço concluído antes da atualização do histórico'
FROM servicos s WHERE s.status = 'CONCLUIDO'
AND NOT EXISTS (SELECT 1 FROM historico_servicos h WHERE h.servico_id = s.id);
