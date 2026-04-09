package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.FuncionarioRepository;

import java.util.ArrayList;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos clientes

public class FuncionarioService {

    // Instancia do repository para acessar os dados dos clientes
    private FuncionarioRepository repository = new FuncionarioRepository();

    // Construtor
    public Funcionario cadastrarFuncionario(String nome, String cpf, String telefone, String email, double salario, Cargo cargo, Status status) {
        Funcionario funcionario = new Funcionario(nome, cpf, telefone, email, salario, cargo, status);
        salvar(funcionario);

        return funcionario;
    }

    // Metodos Crud implementados para realizar as operacoes de salvar, excluir, editar, buscar por ID e listar clientes

    public void salvar(Funcionario funcionario) {
        try {
            repository.salvar(funcionario);
        } catch (Exception e) {
            System.out.println("Erro ao salvar funcionario" + e.getMessage());
        }
    }

    public void excluir(int id) {
        try {
            repository.excluir(id);
        } catch (Exception e) {
            System.out.println("Erro ao remover funcionario" + e.getMessage());
        }
    }

    // Metodo de edicao que busca o cliente existente por ID e atualiza seus dados com os novos valores fornecidos
    public void editar(Funcionario funcionario) {
        try {
            Funcionario funcionarioExistente = buscarPorId(funcionario.getId());

            if (funcionarioExistente != null) {
                funcionarioExistente.setNome(funcionario.getNome());
                funcionarioExistente.setCpf(funcionario.getCpf());
                funcionarioExistente.setTelefone(funcionario.getTelefone());
                funcionarioExistente.setEmail(funcionario.getEmail());
                funcionarioExistente.setSalario(funcionario.getSalario());
                funcionarioExistente.setCargo(funcionario.getCargo());
                funcionarioExistente.setStatus(funcionario.getStatus());

                repository.editar(funcionarioExistente); // Salva no banco a edicao do cliente
            }
        } catch (Exception e) {
            System.out.println("Erro ao editar funcionario: " + e.getMessage());
        }
    }

    public Funcionario buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por id funcionario: " + e.getMessage());
            return null;
        }
    }

    // Metodo para listar todos os clientes cadastrados, retornando uma lista de objetos Cliente
    public List<Funcionario> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            System.out.println("Erro ao listar funcionario: " + e.getMessage());
            return new ArrayList<>(); // Retornando lista vazia para nao crashar no MENU. NullPointerException
        }
    }
}
