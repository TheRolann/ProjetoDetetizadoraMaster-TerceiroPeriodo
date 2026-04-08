package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.interfaces.Crud;

import java.util.ArrayList;
import java.util.List;


// Classe repository, que implementa a interface Crud, e e responsavel por realizar as operacoes de CRUD (Create, Read, Update, Delete) para a entidade Cliente
public class ClienteRepository implements Crud<Cliente>{
    // Crianco o "banco de dados" em memoria
    // Criando a List do tipo Cliente
    private static List<Cliente> clientes = new ArrayList<>();

    // Sobreescrita dos metodos da interface Crud, para realizar as operacoes

    @Override
    public void salvar(Cliente cliente) {
        clientes.add(cliente);
    }

    // Apaga o cliente da lista, caso o id seja encontrado, pelo numero do id, utilizando o metodo buscarPorId para encontrar
    @Override
    public void excluir(int id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            clientes.remove(cliente);
        }
    }

    // Editar vazio, pois a edicao do cliente pode ser feita diretamente na lista, utilizando o metodo buscarPorId para encontrar o cliente e editar seus atributos
    @Override
    public void editar(Cliente cliente) {}

    // Busca o cliente na lista por id com for each, caso nao tenha, retorna null
    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    // Lista todos os clientes da lista, utilizando o metodo listar para retornar a lista de clientes
    public List<Cliente> listar() {
        return clientes;
    }
}
