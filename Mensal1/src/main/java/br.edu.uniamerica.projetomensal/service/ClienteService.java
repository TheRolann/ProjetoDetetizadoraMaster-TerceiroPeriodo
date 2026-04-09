package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.interfaces.Crud;
import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ClienteRepository;

import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos clientes
// Esta classe implementa a interface Crud para fornecer as operacoes basicas de cadastro, exclusao, edicao e consulta de clientes

public class ClienteService implements Crud<Cliente> {

    // Instancia do repository para acessar os dados dos clientes
    private ClienteRepository repository = new ClienteRepository();
    private int proximoId = 1;

    // Construtor padrao que ja inicializa o repository com ID predefinido, ordem crescente
    public Cliente cadastrarCliente(String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
        Cliente cliente = new Cliente(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(cliente);

        return  cliente;
    }

    // Metodos da interface Crud implementados para realizar as operacoes de salvar, excluir, editar, buscar por ID e listar clientes

    @Override
    public void salvar(Cliente cliente) {
        repository.salvar(cliente);
    }

    @Override
    public void excluir(int id) {
        repository.excluir(id);
    }

    // Metodo de edicao que busca o cliente existente por ID e atualiza seus dados com os novos valores fornecidos
    @Override
    public void editar(Cliente cliente) {
        Cliente clienteExistente = buscarPorId(cliente.getId());

        if (clienteExistente != null) {
            clienteExistente.setNomeEmpresa(cliente.getNomeEmpresa());
            clienteExistente.setDocumento(cliente.getDocumento());
            clienteExistente.setEndereco(cliente.getEndereco());
            clienteExistente.setTelefone(cliente.getTelefone());
            clienteExistente.setEmail(cliente.getEmail());
            clienteExistente.setStatus(cliente.getStatus());
        }
    }

    @Override
    public Cliente buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    // Metodo para listar todos os clientes cadastrados, retornando uma lista de objetos Cliente
    @Override
    public List<Cliente> listar() {
        return repository.listar();
    }
}
