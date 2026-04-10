package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.interfaces.Crud;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos clientes
// Esta classe implementa a interface Crud para fornecer as operacoes basicas de cadastro, exclusao, edicao e consulta de clientes

public class FuncionarioService {

    // Instancia do repository para acessar os dados dos clientes
    private FuncionarioRepository repository = new FuncionarioRepository();

    // Construtor
    public Funcionario cadastrarFuncionario(String nome, String cpf, String telefone, String email, double salario, Cargo cargo, Status status) {
        Funcionario funcionario = new Funcionario(nome, cpf, telefone, email, salario, cargo, status);
        salvar(funcionario);
    private EntityManager em;
    private FuncionarioRepository repository;

    public FuncionarioService(EntityManager em) {
        this.em = em;
        this.repository = new FuncionarioRepository(em);
    }

    // Metodos da interface Crud implementados para realizar as operacoes de salvar, excluir, editar, buscar por ID e listar clientes

    @Override
    public void salvar(Funcionario funcionario){
        try {
            em.getTransaction().begin();

            if (funcionario.getId() != null) {
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

    @Override
    public void editar(Funcionario funcionario){
        try {
            em.getTransaction().begin();

            Funcionario existente = repository.buscarPorCpf(funcionario.getCpf());
            if (funcionario.getId() == null) {
                throw new RuntimeException("ID não pode ser nulo para edição");
            }

            if (existente == null) {
                throw new RuntimeException("Funcionario não encontrado");
            }

            Funcionario outroComMesmoCpf = repository.buscarPorCpf(funcionario.getCpf());

            if (outroComMesmoCpf != null && !outroComMesmoCpf.getId().equals(funcionario.getId())) {
                throw new RuntimeException("CPF já cadastrado para outro funcionário");
            }

            existente.setNome(funcionario.getNome());
            existente.setCpf(funcionario.getCpf());
            existente.setTelefone(funcionario.getTelefone());
            existente.setEmail(funcionario.getEmail());
            existente.setSalario(funcionario.getSalario());
            existente.setCargo(funcionario.getCargo());
            existente.setStatus(funcionario.getStatus());

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void excluir(Long id) {
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

    @Override
    public Funcionario buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    // Metodo para listar todos os clientes cadastrados, retornando uma lista de objetos Cliente
    @Override
    public List<Funcionario> listar() {
        return repository.listarAtivos();
    }

    public Funcionario cadastrarFuncionario(String nome, String cpf, String telefone, String email, BigDecimal salario, Cargo cargo) {
        Funcionario funcionario = new Funcionario(
                nome, cpf, telefone, email, salario, cargo, Status.ATIVO
        );
        salvar(funcionario);
        return funcionario;
    }
}
