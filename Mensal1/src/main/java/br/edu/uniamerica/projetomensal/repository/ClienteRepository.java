package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Cliente;
import jakarta.persistence.EntityManager;

import java.util.List;

// Classe repository responsavel por acessar o banco e realizar as operacoes de CRUD para a entidade Cliente
public class ClienteRepository {

    private EntityManager em; // Cria o entity manager para conexao

    // Construtor que aceita EntityManager como parametro
    public ClienteRepository(EntityManager em) {
        this.em = em;
    }

    // Construtor padrao para compatibilidade
    public ClienteRepository() {
        this.em = PersistenceManager.getEntityManager();
    }

    // Salva o cliente no banco
    public void salvar(Cliente cliente) {
        em.persist(cliente);
    }

    // Apaga o cliente do banco caso o id seja encontrado
    public void excluir(int id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            em.remove(cliente);
        }
    }

    // Atualiza o cliente no banco
    public void editar(Cliente cliente) {
        em.merge(cliente);
    }

    // Busca o cliente no banco por id
    public Cliente buscarPorId(int id) {
        return em.find(Cliente.class, id);
    }

    // Lista todos os clientes do banco. Consulta customizada em JPQL (Java Persistence Query Language)
    public List<Cliente> listar() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}
