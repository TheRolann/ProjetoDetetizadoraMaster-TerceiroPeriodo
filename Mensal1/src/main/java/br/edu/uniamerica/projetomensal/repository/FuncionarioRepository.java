package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.EntityManager;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;

import java.util.List;

// OBSOLETO - Classe repository, que implementa a interface Crud, e é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) para a entidade Funcionario
// Classe passa a receber persistencia EntityManager com JPA, sem ser por interface
public class FuncionarioRepository {
    private EntityManager em;

    public FuncionarioRepository(EntityManager em){
        this.em = em;
    }

    public void salvar(Funcionario funcionario) {
        em.persist(funcionario);
    }

    public Funcionario editar(Funcionario funcionario) {

        return em.merge(funcionario);
    }

    // Busca o funcionario na lista por id
    public Funcionario buscarPorId(int id) {
        return em.find(Funcionario.class, id); // Procura na classe Funcionario, o id passado como parametro
    }

    public List<Funcionario> buscarPorNome(String prefoxo) {
    return em.createQuery("SELECT f FROM Funcionario f WHERE f.nome LIKE :prefixo", Funcionario.class)
            .setParameter("prefixo", prefoxo + "%")
            .getResultList();
    }

    // Lista todos os funcionarios
    public List<Funcionario> listar() {
        return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class)
                .getResultList();
    }

    // Lista apenas os funcionarios ativos
    public List<Funcionario> listarAtivos() {
        return em.createQuery(
                "SELECT f FROM Funcionario f WHERE f.status = :status", Funcionario.class)
                .setParameter("status", Status.ATIVO)
                .getResultList();
    }

    public Funcionario buscarPorCpf(String cpf) {
        List<Funcionario> lista = em.createQuery(
                "SELECT f FROM Funcionario f WHERE f.cpf = :cpf", Funcionario.class)
                .setParameter("cpf", cpf)
                .getResultList();
        if (lista.isEmpty()) {
            return null;
        }
        return lista.get(0);
    }

    public boolean existePorCpf(String cpf) {
        Long count = em.createQuery(
                "SELECT COUNT(f) FROM Funcionario f WHERE f.cpf = :cpf", Long.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
        return count > 0;
    }

    public Funcionario autenticar(String nome, String senha) {
        List<Funcionario> lista = em.createQuery(
                        "SELECT f FROM Funcionario f WHERE f.nome = :nome AND f.senha = :senha AND f.status = :status",
                        Funcionario.class)
                .setParameter("nome", nome)
                .setParameter("senha", senha)
                .setParameter("status", Status.ATIVO)
                .getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

}

