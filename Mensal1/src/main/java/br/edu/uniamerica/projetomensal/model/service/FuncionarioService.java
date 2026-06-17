package br.edu.uniamerica.projetomensal.model.service;

import br.edu.uniamerica.projetomensal.controller.funcionario.FuncionarioRequest;
import br.edu.uniamerica.projetomensal.model.entity.FuncionarioEntity;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.model.repository.FuncionarioRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioService {
    // Gerencia a transacao com o banco
    private final EntityManager em;
    // Repository que acessa os funcionarios no banco
    private final FuncionarioRepository repository;

    public FuncionarioService(EntityManager em) {
        this.em = em;
        this.repository = new FuncionarioRepository(em);
    }

    // Salva um novo funcionario
    // Verifica regras basicas: id deve ser zero (novo), CPF deve ser unico
    // Usa transacao para garantir atomicidade
    public void salvar(FuncionarioRequest funcionarioRequest) {
        try {
            em.getTransaction().begin();
            FuncionarioEntity funcionario = new FuncionarioEntity();

            funcionario.setNome(funcionarioRequest.nome());
            funcionario.setCpf(funcionarioRequest.cpf());
            funcionario.setTelefone(funcionarioRequest.telefone());
            funcionario.setEmail(funcionarioRequest.email());
            funcionario.setEndereco(funcionarioRequest.endereco());
            funcionario.setSalario(funcionarioRequest.salario());
            funcionario.setCargo(funcionarioRequest.cargo());
            funcionario.setStatus(funcionarioRequest.status());
            funcionario.setSenha(funcionarioRequest.senha());

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

    // Edita um funcionario existente
    // Verifica que o id existe e que o CPF nao esta duplicado para outro registro
    public void editar(int idFuncionario, FuncionarioRequest funcionarioRequest) {
        try {
            em.getTransaction().begin();

            FuncionarioEntity existente = repository.buscarPorId(idFuncionario);
            if (existente == null) {
                throw new NegocioException("Funcionario não encontrado");
            }

            FuncionarioEntity outroComMesmoCpf = repository.buscarPorCpf(funcionarioRequest.cpf());

            if (outroComMesmoCpf != null && outroComMesmoCpf.getId() != existente.getId()) {
                throw new NegocioException("CPF já cadastrado para outro funcionário");
            }

            existente.setNome(funcionarioRequest.nome());
            existente.setCpf(funcionarioRequest.cpf());
            existente.setTelefone(funcionarioRequest.telefone());
            existente.setEmail(funcionarioRequest.email());
            existente.setEndereco(funcionarioRequest.endereco());
            existente.setSalario(funcionarioRequest.salario());
            existente.setCargo(funcionarioRequest.cargo());
            existente.setStatus(funcionarioRequest.status());

            if (funcionarioRequest.senha() != null && !funcionarioRequest.senha().isEmpty()) {
                existente.setSenha(funcionarioRequest.senha());
            }

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

    // Marca o funcionario como inativo em vez de apagar
    // Valida se o funcionario existe e se ja nao esta inativo
    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            FuncionarioEntity funcionario = repository.buscarPorId(id);
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

    public FuncionarioEntity buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar funcionario por ID: ", e);
        }
    }

    public List<FuncionarioEntity> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao listar funcionarios: ", e);
        }
    }

    // Autentica funcionario por nome e senha
    // Retorna o funcionario ativo correspondente ou null
    public FuncionarioEntity autenticar(String nome, String senha) {
        try {
            return repository.autenticar(nome, senha);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar: ", e);
        }
    }

}
