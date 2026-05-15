package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Cliente;
import jakarta.persistence.EntityManager;

import java.util.List;

// Classe responsavel por acessar e manipular os dados de Cliente no banco
// Aqui ficam as operacoes basicas de persistencia usando JPA
public class ClienteRepository {

    // EntityManager responsavel por conversar com o banco de dados
    private final EntityManager em;

    // Recebe o EntityManager no construtor para reutilizar nas operacoes
    public ClienteRepository(EntityManager em) {
        this.em = em;
    }

    // Salva um novo cliente no banco
    public void salvar(Cliente cliente) {
        em.persist(cliente); // persist adiciona a entidade no contexto de persistencia
    }

    // Exclui um cliente pelo id
    // Primeiro busca o registro, depois remove se ele existir
    public void excluir(int id) {
        Cliente cliente = buscarPorId(id); // Busca o cliente pela chave primaria
        if (cliente != null) {
            em.remove(cliente); // remove apaga a entidade do banco
        }
    }

    // Atualiza os dados de um cliente ja existente
    public void editar(Cliente cliente) {
        em.merge(cliente); // merge sincroniza as alteracoes com o banco
    }

    // Busca um cliente pelo id
    // Retorna null se nao encontrar
    public Cliente buscarPorId(int id) {
        return em.find(Cliente.class, id); // find procura pela chave primaria
    }

    // Lista todos os clientes cadastrados
    public List<Cliente> listar() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}