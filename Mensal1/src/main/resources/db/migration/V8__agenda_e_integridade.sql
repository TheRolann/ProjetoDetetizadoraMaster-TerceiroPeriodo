-- Preserva o histórico ao concluir ou inativar um serviço.
CREATE OR REPLACE FUNCTION fn_atualizar_agenda_servico()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status IN ('CONCLUIDO', 'INATIVO') AND OLD.status IS DISTINCT FROM NEW.status THEN
        UPDATE agenda SET status = NEW.status
        WHERE servico_id = NEW.id AND status IN ('AGENDADO', 'EM_ANDAMENTO');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Reforça o bloqueio de dois agendamentos simultâneos do responsável.
CREATE UNIQUE INDEX agenda_responsavel_horario
ON agenda (funcionario_id, data_agendada, hora)
WHERE status IN ('AGENDADO', 'EM_ANDAMENTO');
