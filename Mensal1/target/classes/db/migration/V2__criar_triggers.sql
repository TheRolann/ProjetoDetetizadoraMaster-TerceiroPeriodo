-- =====================
-- TRIGGERS E FUNCOES
-- =====================

-- Trigger para Registar Historico
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_registrar_historico_servico()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'CONCLUIDO' AND OLD.status != 'CONCLUIDO' THEN
        INSERT INTO historico_servicos (servico_id, observacao)
        VALUES (NEW.id, 'Servico concluido automaticamente por trigger');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_historico_servico ON servicos;
CREATE TRIGGER trg_historico_servico
    AFTER UPDATE ON servicos
    FOR EACH ROW
EXECUTE FUNCTION fn_registrar_historico_servico();

-- Trigger para validar Status do Funcionario
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_validar_status_funcionario()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'INATIVO' THEN
        RAISE EXCEPTION 'Nao e permitido cadastrar funcionario INATIVO';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_validar_status_funcionario ON funcionarios;
CREATE TRIGGER trg_validar_status_funcionario
    BEFORE INSERT ON funcionarios
    FOR EACH ROW
EXECUTE FUNCTION fn_validar_status_funcionario();

-- Trigger para atualizar agenda quando servico finalizado
-- Funcao do Trigger
CREATE OR REPLACE FUNCTION fn_atualizar_agenda_servico()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'CONCLUIDO' AND OLD.status != 'CONCLUIDO' THEN
        UPDATE agenda
        SET status = 'CONCLUIDO'
        WHERE servico_id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
DROP TRIGGER IF EXISTS trg_atualizar_agenda_servico ON servicos;
CREATE TRIGGER trg_atualizar_agenda_servico
    AFTER UPDATE ON servicos
    FOR EACH ROW
EXECUTE FUNCTION fn_atualizar_agenda_servico();