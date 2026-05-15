package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioService {

    private EntityManager em;
    private FuncionarioRepository repository;

    public FuncionarioService(EntityManager em) {
        this.em = em;
        this.repository = new FuncionarioRepository(em);
    }

    public void salvar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();

            if (funcionario.getId() != 0) {
                throw new NegocioException("Novo funcionário não pode ter ID");
            }

            if (repository.existePorCpf(funcionario.getCpf())) {
                throw new NegocioException("Já existe funcionário com esse CPF");
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
                throw new NegocioException("ID não pode ser zero para edição");
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

    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Funcionario funcionario = repository.buscarPorId(id);
            if (funcionario == null) {
                throw new NegocioException("Funcionario não encontrado");
            }
            if (funcionario.getStatus() == Status.INATIVO) {
                throw new NegocioException("Funcionario já está inativo");
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

    public Funcionario autenticar(String nome, String senha) {
        try {
            return repository.autenticar(nome, senha);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar: ", e);
        }
    }

}
