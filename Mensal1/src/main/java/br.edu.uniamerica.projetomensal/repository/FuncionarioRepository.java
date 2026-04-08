package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import jakarta.persistence.EntityManager;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;

import java.util.List;

// OBSOLETO - Classe repository, que implementa a interface Crud, e é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) para a entidade Funcionario
// Classe passa a receber persistencia EntityManager com JPA, sem ser por interface
public class FuncionarioRepository {

    private EntityManager em = PersistenceManager.getEntityManager();

    public void salvar(Funcionario funcionario) {
        em.getTransaction().begin(); // Comeca a transacao
        em.persist(funcionario);     // Salva no banco
        em.getTransaction().commit();// Termina a operaaco
    }

    // Apaga o funcionario da lista, caso o id seja encontrado pelo numero do id, utilizando o metodo buscarPorId para encontrar
    public void excluir(int id) {
        Funcionario funcionario = buscarPorId(id);
        if (funcionario != null) {
            em.getTransaction().begin();
            em.remove(funcionario);      // Remove o funcionario do banco
            em.getTransaction().commit();
        }
    }

    public void editar(Funcionario funcionario) {
        em.getTransaction().begin();
        em.merge(funcionario);       // Atualiza o estado da tabela no banco
        em.getTransaction().commit();
    }

    // Busca o funcionario na lista por id
    public Funcionario buscarPorId(int id) {
        return em.find(Funcionario.class, id); // Procura na classe Funcionario, o id passado como parametro
    }

    // Lista todos os funcionarios da lista, utilizando o metodo listar para retornar a lista de funcionarios
    public List<Funcionario> listar() {
        return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class).getResultList();
    }
}
