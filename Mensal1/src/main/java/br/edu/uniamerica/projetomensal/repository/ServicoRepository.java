package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Servico;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ServicoRepository {

    private EntityManager em;

    public ServicoRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Servico servico) {
        em.persist(servico);
    }

    public void excluir(int id) {
        Servico servico = buscarPorId(id);
        if (servico != null) {
            em.remove(servico);
        }
    }

    public void editar(Servico servico) {
        em.merge(servico);
    }

    public Servico buscarPorId(int id) {
        return em.find(Servico.class, id);
    }

    public List<Servico> listar() {
        return em.createQuery("SELECT s FROM Servico s", Servico.class).getResultList();
    }
}