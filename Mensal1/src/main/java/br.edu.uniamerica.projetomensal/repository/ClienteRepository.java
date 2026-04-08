package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.interfaces.Crud;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;


// OBSOLETO - Classe repository, que implementa a interface Crud, e e responsavel por realizar as operacoes de CRUD (Create, Read, Update, Delete) para a entidade Cliente
// Classe passa a receber persistencia EntityManager com JPA, sem ser por interface
public class ClienteRepository {
    // Crianco o "banco de dados" em memoria
    // Criando a List do tipo Cliente
    private static List<Cliente> clientes = new ArrayList<>();

    private EntityManager em = PersistenceManager.getEntityManager();

    // Sobreescrita dos metodos da interface Crud, para realizar as operacoes

    public void salvar(Cliente cliente) {
        em.getTransaction().begin(); // comeca a transacao
        em.persist(cliente);         // operacao (persist, merge, remove) persist = salva no banco
        em.getTransaction().commit();// commit = confirma a transacao
    }

    // Apaga o cliente da lista, caso o id seja encontrado, pelo numero do id, utilizando o metodo buscarPorId para encontrar
    @Override
    public void excluir(int id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            clientes.remove(cliente);
        }
    }

    // Editar vazio, pois a edicao do cliente pode ser feita diretamente na lista, utilizando o metodo buscarPorId para encontrar o cliente e editar seus atributos
    @Override
    public void editar(Cliente cliente) {}

    // Busca o cliente na lista por id com for each, caso nao tenha, retorna null
    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    // Lista todos os clientes da lista, utilizando o metodo listar para retornar a lista de clientes
    public List<Cliente> listar() {
        return clientes;
    }
}
