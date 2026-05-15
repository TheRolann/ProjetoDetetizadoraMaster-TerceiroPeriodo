package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioService {

    private EntityManager em;
    private FuncionarioRepository repository;

    public FuncionarioService(EntityManager em) {
        this.em = em;
        this.repository = new FuncionarioRepository(em);
    }

    // Metodos da interface Crud implementados para realizar as operacoes de salvar, excluir, editar, buscar por ID e listar funcionarios

    public void salvar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();

            if (funcionario.getId() != 0) {
                throw new RuntimeException("Novo funcionário não pode ter ID");
            }

            if (repository.existePorCpf(funcionario.getCpf())) {
                throw new RuntimeException("Já existe funcionário com esse CPF");
            }

            funcionario.setStatus(Status.ATIVO);

            repository.salvar(funcionario);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void editar(Funcionario funcionario) {
        try {
            em.getTransaction().begin();

            if (funcionario.getId() == 0) {
                throw new RuntimeException("ID não pode ser zero para edição");
            }

            Funcionario existente = repository.buscarPorId(funcionario.getId());
            if (existente == null) {
                throw new RuntimeException("Funcionario não encontrado");
            }

            Funcionario outroComMesmoCpf = repository.buscarPorCpf(funcionario.getCpf());

            if (outroComMesmoCpf != null && outroComMesmoCpf.getId() != funcionario.getId()) {
                throw new RuntimeException("CPF já cadastrado para outro funcionário");
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
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Funcionario funcionario = repository.buscarPorId(id);
            if (funcionario == null) {
                throw new RuntimeException("Funcionario não encontrado");
            }
            if (funcionario.getStatus() == Status.INATIVO) {
                throw new RuntimeException("Funcionario já está inativo");
            }
            funcionario.setStatus(Status.INATIVO);

            repository.editar(funcionario);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Funcionario buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Funcionario> listar() {
        return repository.listar();
    }

    public Funcionario autenticar(String nome, String senha) {
        try {
            return repository.autenticar(nome, senha);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar: ", e);
        }
    }

}
