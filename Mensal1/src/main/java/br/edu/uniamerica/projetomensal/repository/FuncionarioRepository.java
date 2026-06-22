package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioRepository {
    private final EntityManager em;

    public FuncionarioRepository(EntityManager em){
        this.em = em;
    }

    public void salvar(Funcionario funcionario) {
        em.persist(funcionario);
    }

    public Funcionario editar(Funcionario funcionario) {
        return em.merge(funcionario);
    }

    public Funcionario buscarPorId(int id) {
        return em.find(Funcionario.class, id);
    }

    public List<Funcionario> listar() {
        return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class)
                .getResultList();
    }

    public Funcionario buscarPorCpf(String cpf) {
        List<Funcionario> lista = em.createQuery(
                        "SELECT f FROM Funcionario f WHERE f.cpf = :cpf", Funcionario.class)
                .setParameter("cpf", cpf)
                .getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

    public boolean existePorCpf(String cpf) {
        Long count = em.createQuery(
                        "SELECT COUNT(f) FROM Funcionario f WHERE f.cpf = :cpf", Long.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
        return count > 0;
    }

    // Busca um funcionario ATIVO pelo nome, sem checar a senha.
    // A senha e hash (BCrypt) e cada hash de uma mesma senha e diferente
    // por causa do salt aleatorio, entao NUNCA da pra comparar via WHERE no SQL.
    // A comparacao precisa ser feita em Java com Funcionario.senhaCorreta().
    public Funcionario buscarAtivoPorNome(String nome) {
        List<Funcionario> lista = em.createQuery(
                        "SELECT f FROM Funcionario f WHERE f.nome = :nome AND f.status = :status",
                        Funcionario.class)
                .setParameter("nome", nome)
                .setParameter("status", Status.ATIVO)
                .getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }
}
