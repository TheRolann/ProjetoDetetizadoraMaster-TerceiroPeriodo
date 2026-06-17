package br.edu.uniamerica.projetomensal.model.repository;

import br.edu.uniamerica.projetomensal.model.entity.FuncionarioEntity;
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
    public void salvar(FuncionarioEntity funcionarioEntity) {
        em.persist(funcionarioEntity);
    }

    // Atualiza os dados de um funcionario existente
    public FuncionarioEntity editar(FuncionarioEntity funcionarioEntity) {

        return em.merge(funcionarioEntity);
    }

    // Busca um funcionario pelo id
    public FuncionarioEntity buscarPorId(int id) {
        return em.find(FuncionarioEntity.class, id); // Procura pelo id informado
    }

    // Busca funcionarios cujo nome comeca com o texto informado
    public List<FuncionarioEntity> buscarPorNome(String prefoxo) {
    return em.createQuery("SELECT f FROM FuncionarioEntity f WHERE f.nome LIKE :prefixo", FuncionarioEntity.class)
            .setParameter("prefixo", prefoxo + "%")
            .getResultList();
    }

    // Lista todos os funcionarios cadastrados
    public List<FuncionarioEntity> listar() {
        return em.createQuery("SELECT f FROM FuncionarioEntity f", FuncionarioEntity.class)
                .getResultList();
    }

    // Lista apenas os funcionarios com status ativo
    public List<FuncionarioEntity> listarAtivos() {
        return em.createQuery(
                "SELECT f FROM FuncionarioEntity f WHERE f.status = :status", FuncionarioEntity.class)
                .setParameter("status", Status.ATIVO)
                .getResultList();
    }

    // Busca um funcionario pelo CPF
    public FuncionarioEntity buscarPorCpf(String cpf) {
        List<FuncionarioEntity> lista = em.createQuery(
                "SELECT f FROM FuncionarioEntity f WHERE f.cpf = :cpf", FuncionarioEntity.class)
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
                "SELECT COUNT(f) FROM FuncionarioEntity f WHERE f.cpf = :cpf", Long.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
        return count > 0;
    }

    // Autentica um funcionario pelo nome e pela senha
    public FuncionarioEntity autenticar(String nome, String senha) {
        List<FuncionarioEntity> lista = em.createQuery(
                        "SELECT f FROM FuncionarioEntity f WHERE f.nome = :nome AND f.senha = :senha AND f.status = :status",
                        FuncionarioEntity.class)
                .setParameter("nome", nome)
                .setParameter("senha", senha)
                .setParameter("status", Status.ATIVO)
                .getResultList();
        return lista.isEmpty() ? null : lista.get(0);
    }

}

