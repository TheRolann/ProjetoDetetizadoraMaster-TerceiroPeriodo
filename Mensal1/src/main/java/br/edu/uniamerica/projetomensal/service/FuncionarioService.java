package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import br.edu.uniamerica.projetomensal.config.Sessao;
import br.edu.uniamerica.projetomensal.utils.ValidacaoCadastro;
import br.edu.uniamerica.projetomensal.utils.Transacoes;

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
        Sessao.exigirGerente();
        ValidacaoCadastro.funcionario(funcionario);
        try {
            em.getTransaction().begin();

            if (funcionario.getId() != 0) {
                throw new NegocioException("Novo funcionário não pode ter ID");
            }
            if (repository.existePorCpf(funcionario.getCpf())) {
                throw new NegocioException("Já existe funcionário com esse CPF");
            }

            if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) throw new NegocioException("Senha é obrigatória.");
            funcionario.setStatus(Status.ATIVO);
            repository.salvar(funcionario);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            Transacoes.desfazer(em);
            throw new RuntimeException("Erro inesperado ao salvar funcionario: ", e);
        }
    }

    public void editar(Funcionario funcionario) {
        Sessao.exigirGerente();
        ValidacaoCadastro.funcionario(funcionario);
        try {
            em.getTransaction().begin();

            if (funcionario.getId() == 0) {
                throw new NegocioException("Selecione um funcionário para editar.");
            }

            Funcionario existente = repository.buscarPorId(funcionario.getId());
            if (existente == null) {
                throw new NegocioException("Funcionario não encontrado");
            }

            if (existente.getId() == Sessao.usuario().getId() &&
                    (funcionario.getStatus() != Status.ATIVO || funcionario.getCargo() != br.edu.uniamerica.projetomensal.model.enums.Cargo.GERENTE)) {
                throw new NegocioException("Você não pode retirar seu próprio acesso de gerente.");
            }
            Funcionario outroComMesmoCpf = repository.buscarPorCpf(funcionario.getCpf());
            if (outroComMesmoCpf != null && outroComMesmoCpf.getId() != funcionario.getId()) {
                throw new NegocioException("CPF já cadastrado para outro funcionário");
            }

            if (funcionario.getStatus() == Status.INATIVO && repository.temAgendaPendente(funcionario.getId())) throw new NegocioException("Reatribua ou cancele os agendamentos pendentes antes de inativar o funcionário.");
            existente.setNome(funcionario.getNome());
            existente.setCpf(funcionario.getCpf());
            existente.setTelefone(funcionario.getTelefone());
            existente.setEmail(funcionario.getEmail());
            existente.setEndereco(funcionario.getEndereco());
            existente.setSalario(funcionario.getSalario());
            existente.setCargo(funcionario.getCargo());
            existente.setStatus(funcionario.getStatus());

            if (funcionario.getSenha() != null) existente.setSenhaHash(funcionario.getSenha());

            repository.editar(existente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            Transacoes.desfazer(em);
            throw new RuntimeException("Erro inesperado ao editar funcionario: ", e);
        }
    }

    // Marca o funcionario como inativo em vez de apagar (soft delete)
    public void excluir(int id) {
        Sessao.exigirGerente();
        try {
            em.getTransaction().begin();

            Funcionario funcionario = repository.buscarPorId(id);
            if (funcionario == null) {
                throw new NegocioException("Funcionário não encontrado.");
            }
            if (funcionario.getStatus() == Status.INATIVO) {
                throw new NegocioException("Funcionário já está inativo.");
            }
            if (funcionario.getId() == Sessao.usuario().getId()) throw new NegocioException("Você não pode inativar seu próprio acesso.");
            if (repository.temAgendaPendente(id)) throw new NegocioException("Reatribua ou cancele os agendamentos pendentes antes de inativar o funcionário.");
            funcionario.setStatus(Status.INATIVO);

            repository.editar(funcionario);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            Transacoes.desfazer(em);
            throw new NegocioException("Erro inesperado ao excluir funcionario: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) {
        Sessao.exigirGerente();
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar funcionario por ID: ", e);
        }
    }

    public List<Funcionario> listar() {
        Sessao.exigirGerente();
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
