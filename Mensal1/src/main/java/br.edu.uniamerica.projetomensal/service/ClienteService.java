package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos clientes

public class ClienteService {

    // Instancia do repository para acessar os dados dos clientes
    private ClienteRepository repository = new ClienteRepository();

    // Construtor
    public Cliente cadastrarCliente(String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
        Cliente cliente = new Cliente(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(cliente);

        return  cliente;
    }

    public void salvar(Cliente cliente) {
        try {
            // Validação de documento válido (CPF/CNPJ)
            if (cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
                throw new RuntimeException("Documento não pode estar vazio");
            }
            if (!cliente.getDocumento().matches("\\d{11}|\\d{14}")) {
                throw new RuntimeException("Documento deve conter exatamente 11 ou 14 dígitos");
            }

            repository.salvar(cliente);
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        try {
            repository.excluir(id);
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    // Metodo de edicao que busca o cliente existente por ID e atualiza seus dados com os novos valores fornecidos
    public void editar(Cliente cliente) {
        try {
            Cliente clienteExistente = buscarPorId(cliente.getId());

            if (clienteExistente != null) {
                clienteExistente.setNomeEmpresa(cliente.getNomeEmpresa());
                clienteExistente.setDocumento(cliente.getDocumento());
                clienteExistente.setEndereco(cliente.getEndereco());
                clienteExistente.setTelefone(cliente.getTelefone());
                clienteExistente.setEmail(cliente.getEmail());
                clienteExistente.setStatus(cliente.getStatus());

                repository.editar(clienteExistente); // Salva no banco a edicao do cliente
            }
        } catch (Exception e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por id: " + e.getMessage());
            return null;
        }
    }

    // Metodo para listar todos os clientes cadastrados, retornando uma lista de Cliente
    public List<Cliente> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e.getMessage());
            return new ArrayList<>(); // Retornando lista vazia para nao crashar no MENU. NullPointerException
        }
    }
}
