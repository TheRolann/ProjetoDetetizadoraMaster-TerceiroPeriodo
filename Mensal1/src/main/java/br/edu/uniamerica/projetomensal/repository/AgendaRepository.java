package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;

public class AgendaRepository {
    private final EntityManager em;
    public AgendaRepository(EntityManager em) { this.em = em; }
    public List<Agenda> listar() {
        return em.createQuery("SELECT a FROM Agenda a JOIN FETCH a.servico s JOIN FETCH s.clienteEntity LEFT JOIN FETCH a.funcionario ORDER BY a.data, a.hora", Agenda.class).getResultList();
    }
    public Agenda buscar(int id) { return em.find(Agenda.class, id); }
    public void salvar(Agenda agenda) { em.persist(agenda); }
    public boolean conflito(int id, int funcionario, LocalDate data, LocalTime hora) {
        return em.createQuery("SELECT COUNT(a) FROM Agenda a WHERE a.id <> :id AND a.funcionario.id = :funcionario AND a.data = :data AND a.hora = :hora AND a.status IN (:agendado, :andamento)", Long.class)
                .setParameter("id", id).setParameter("funcionario", funcionario).setParameter("data", data).setParameter("hora", hora)
                .setParameter("agendado", Status.AGENDADO).setParameter("andamento", Status.EM_ANDAMENTO).getSingleResult() > 0;
    }
    public List<Funcionario> responsaveis() {
        return em.createQuery("SELECT f FROM Funcionario f ORDER BY f.nome", Funcionario.class)
                .getResultList();
    }
}
