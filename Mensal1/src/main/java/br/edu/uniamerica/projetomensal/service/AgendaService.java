package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.config.Sessao;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.*;
import br.edu.uniamerica.projetomensal.utils.*;
import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;

public class AgendaService {
    private final EntityManager em;
    private final AgendaRepository repository;
    public AgendaService(EntityManager em) { this.em = em; repository = new AgendaRepository(em); }
    public List<Agenda> listar() { Sessao.exigirLogin(); return repository.listar(); }
    public List<Funcionario> responsaveis() { Sessao.exigirLogin(); return repository.responsaveis(); }
    public void salvar(int id, int servicoId, int funcionarioId, LocalDate data, LocalTime hora, String observacoes, Status status) {
        Sessao.exigirLogin();
        if (status == Status.INATIVO) Sessao.exigirGerente();
        if (data == null || hora == null) throw new NegocioException("Informe data e horário válidos.");
        hora = hora.withSecond(0).withNano(0);
        if (status != Status.AGENDADO && status != Status.EM_ANDAMENTO && status != Status.CONCLUIDO && status != Status.INATIVO) throw new NegocioException("Status inválido.");
        boolean pendente = status == Status.AGENDADO || status == Status.EM_ANDAMENTO;
        if (pendente && LocalDateTime.of(data, hora).isBefore(LocalDateTime.now())) throw new NegocioException("O agendamento pendente deve ser no futuro.");
        try {
            em.getTransaction().begin();
            Servico servico = new ServicoRepository(em).buscarPorId(servicoId);
            Funcionario funcionario = new FuncionarioRepository(em).buscarPorId(funcionarioId);
            if (servico == null || funcionario == null) throw new NegocioException("Selecione serviço e responsável cadastrados.");
            if (pendente && (servico.getStatus() == Status.CONCLUIDO || servico.getStatus() == Status.INATIVO || funcionario.getStatus() != Status.ATIVO)) throw new NegocioException("Use serviço pendente e responsável ativo.");
            if (pendente && repository.conflito(id, funcionarioId, data, hora)) throw new NegocioException("O responsável já tem um agendamento nesse horário.");
            Agenda agenda = id == 0 ? new Agenda() : repository.buscar(id);
            if (agenda == null) throw new NegocioException("Agendamento não encontrado.");
            agenda.setServico(servico); agenda.setFuncionario(funcionario); agenda.setData(data); agenda.setHora(hora.withSecond(0).withNano(0));
            agenda.setObservacoes(observacoes); agenda.setStatus(status);
            if (id == 0) repository.salvar(agenda);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Transacoes.desfazer(em);
            if (ex instanceof NegocioException negocio) throw negocio;
            throw new NegocioException("Não foi possível salvar o agendamento. Atualize a lista e confira os vínculos.");
        }
    }
    public void cancelar(int id) {
        Sessao.exigirGerente();
        try {
            em.getTransaction().begin();
            Agenda agenda = repository.buscar(id);
            if (agenda == null) throw new NegocioException("Agendamento não encontrado.");
            if (agenda.getStatus() == Status.CONCLUIDO) throw new NegocioException("Agendamento concluído deve permanecer no histórico.");
            agenda.setStatus(Status.INATIVO);
            em.getTransaction().commit();
        } catch (Exception ex) { Transacoes.desfazer(em); throw ex; }
    }
}
