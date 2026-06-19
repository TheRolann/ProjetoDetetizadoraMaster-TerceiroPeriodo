package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioService {
    private final EntityManager em;
    private final FuncionarioRepository repository;

    public FuncionarioService(EntityManager em) {
        this.em = em;
        this.repository = new FuncionarioRepository(em);
    }

    // Salva um novo funcionario. A senha ja chega como hash dentro do objeto
    // Funcionario, pois o hash e gerado em Funcionario.setSenha() no momento
    // em que o Controller monta o objeto.
    public void salvar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();

            if (funcionario.getId() != 0) {
                throw new RuntimeException("Novo funcionario nao pode ter ID");
            }
            if (repository.existePorCpf(funcionario.getCpf())) {
                throw new RuntimeException("Ja existe funcionario com esse CPF");
            }

            funcionario.setStatus(Status.ATIVO);
            repository.salvar(funcionario);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro inesperado ao salvar funcionario: ", e);
        }
    }

    public void editar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();

            if (funcionario.getId() == 0) {
                throw new RuntimeException("ID nao pode ser zero para edicao");
            }

            Funcionario existente = repository.buscarPorId(funcionario.getId());
            if (existente == null) {
                throw new NegocioException("Funcionario não encontrado");
            }

            Funcionario outroComMesmoCpf = repository.buscarPorCpf(funcionario.getCpf());
            if (outroComMesmoCpf != null && outroComMesmoCpf.getId() != funcionario.getId()) {
                throw new NegocioException("CPF já cadastrado para outro funcionário");
            }

            existente.setNome(funcionario.getNome());
            existente.setCpf(funcionario.getCpf());
            existente.setTelefone(funcionario.getTelefone());
            existente.setEmail(funcionario.getEmail());
            existente.setEndereco(funcionario.getEndereco());
            existente.setSalario(funcionario.getSalario());
            existente.setCargo(funcionario.getCargo());
            existente.setStatus(funcionario.getStatus());

            // O hash da senha NAO e tratado aqui de proposito: o FuncionarioController
            // ja busca essa mesma entidade gerenciada (mesmo EntityManager) e so chama
            // funcionario.setSenha(novaSenha) quando o usuario digitou uma nova senha.
            // Como "existente" e o mesmo objeto que o controller alterou, o hash novo
            // (se houver) ja esta presente; senao, o hash antigo do banco permanece.

            repository.editar(existente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro inesperado ao editar funcionario: ", e);
        }
    }

    // Marca o funcionario como inativo em vez de apagar (soft delete)
    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Funcionario funcionario = repository.buscarPorId(id);
            if (funcionario == null) {
                throw new RuntimeException("Funcionario nao encontrado");
            }
            if (funcionario.getStatus() == Status.INATIVO) {
                throw new RuntimeException("Funcionario ja esta inativo");
            }
            funcionario.setStatus(Status.INATIVO);

            repository.editar(funcionario);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new NegocioException("Erro inesperado ao excluir funcionario: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar funcionario por ID: ", e);
        }
    }

    public List<Funcionario> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao listar funcionarios: ", e);
        }
    }

    // Autentica funcionario por nome e senha: busca por nome (ATIVO) e
    // confere a senha informada contra o hash salvo via senhaCorreta().
    public Funcionario autenticar(String nome, String senha) {
        try {
            Funcionario funcionario = repository.buscarAtivoPorNome(nome);
            if (funcionario == null) {
                return null;
            }
            return funcionario.senhaCorreta(senha) ? funcionario : null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar: ", e);
        }
    }
}
