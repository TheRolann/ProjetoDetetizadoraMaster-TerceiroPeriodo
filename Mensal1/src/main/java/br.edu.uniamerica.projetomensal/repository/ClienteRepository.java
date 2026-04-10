package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Cliente;
import jakarta.persistence.EntityManager;

import java.util.List;

// OBSOLETO - Classe repository, que implementa a interface Crud, e e responsavel por realizar as operacoes de CRUD (Create, Read, Update, Delete) para a entidade Cliente
// Classe passa a receber persistencia EntityManager com JPA, sem ser por interface
public class ClienteRepository {

    private EntityManager em = PersistenceManager.getEntityManager();

    // Sobreescrita dos metodos da interface Crud, para realizar as operacoes

    public void salvar(Cliente cliente) {
        em.getTransaction().begin(); // comeca a transacao
        em.persist(cliente);         // operacao (persist, merge, remove) persist = salva no banco
        em.getTransaction().commit();// commit = confirma a transacao
    }

    // Apaga o cliente da lista, caso o id seja encontrado pelo numero do id, utilizando o metodo buscarPorId para encontrar
    public void excluir(int id) {
        Cliente cliente = buscarPorId(id); // Busca o cliente
        if (cliente != null) {
            em.getTransaction().begin();
            em.remove(cliente);           // Remove o cliente encontrado
            em.getTransaction().commit();
        }
    }

    public void editar(Cliente cliente) {
        em.getTransaction().begin();
        em.merge(cliente);            // Mescla o cliente atualizado com o banco
        em.getTransaction().commit();
    }

    // Busca o cliente no banco por id
    public Cliente buscarPorId(int id) {
        return em.find(Cliente.class, id); // Procura na classe Cliennte, o id passado como parametro
    }

    // Lista todos os clientes do banco
    public List<Cliente> listar() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}
