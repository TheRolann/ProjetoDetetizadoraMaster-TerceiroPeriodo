package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Cliente;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClienteRepository {

    private EntityManager em;

    public ClienteRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Cliente cliente) {
        em.persist(cliente);
    }

    public void excluir(int id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            em.remove(cliente);
        }
    }

    public void editar(Cliente cliente) {
        em.merge(cliente);
    }

    public Cliente buscarPorId(int id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> listar() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}