package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.EntityManager;

import java.util.List;

// Classe responsavel por acessar e manipular funcionarios no banco
// Aqui ficam as operacoes de cadastro, consulta, edicao e autenticacao
public class FuncionarioRepository {
    // Objeto que conversa com o banco de dados
    private final EntityManager em;

    // Recebe o EntityManager pronto para usar nas consultas
    public FuncionarioRepository(EntityManager em){
        this.em = em;
    }

    // Salva um novo funcionario no banco
    public void salvar(Funcionario funcionario) {
        em.persist(funcionario);
    }

    // Atualiza os dados de um funcionario existente
    public Funcionario editar(Funcionario funcionario) {

        return em.merge(funcionario);
    }

    // Busca um funcionario pelo id
    public Funcionario buscarPorId(int id) {
        return em.find(Funcionario.class, id); // Procura pelo id informado
    }

    // Busca funcionarios cujo nome comeca com o texto informado
    public List<Funcionario> buscarPorNome(String prefoxo) {
    return em.createQuery("SELECT f FROM Funcionario f WHERE f.nome LIKE :prefixo", Funcionario.class)
            .setParameter("prefixo", prefoxo + "%")
            .getResultList();
    }

    // Lista todos os funcionarios cadastrados
    public List<Funcionario> listar() {
        return em.createQuery("SELECT f FROM Funcionario f", Funcionario.class)
                .getResultList();
    }

    // Lista apenas os funcionarios com status ativo
    public List<Funcionario> listarAtivos() {
        return em.createQuery(
                "SELECT f FROM Funcionario f WHERE f.status = :status", Funcionario.class)
                .setParameter("status", Status.ATIVO)
                .getResultList();
    }

    // Busca um funcionario pelo CPF
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

    // Verifica se ja existe funcionario com o CPF informado
    public boolean existePorCpf(String cpf) {
        Long count = em.createQuery(
                "SELECT COUNT(f) FROM Funcionario f WHERE f.cpf = :cpf", Long.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
        return count > 0;
    }

    // Autentica um funcionario pelo nome e pela senha
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

