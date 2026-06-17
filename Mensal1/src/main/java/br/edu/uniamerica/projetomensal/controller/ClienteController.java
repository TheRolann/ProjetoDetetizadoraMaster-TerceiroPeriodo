package br.edu.uniamerica.projetomensal.controller;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import java.util.List;

// Controller recebe as acoes da View e delega ao Service
// Nao conhece Swing, nao importa nada de javax.swing
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(EntityManager em) {
        this.clienteService = new ClienteService(em);
    }

    public void salvar(String nomeEmpresa, String documento, String endereco,
                       String telefone, String email, Status status) {
        clienteService.cadastrarCliente(nomeEmpresa, documento, endereco, telefone, email, status);
    }

    public void editar(int id, String nomeEmpresa, String documento, String endereco,
                       String telefone, String email, Status status) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) throw new NegocioException("Cliente não encontrado");

        cliente.setNomeEmpresa(nomeEmpresa);
        cliente.setDocumento(documento);
        cliente.setEndereco(endereco);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setStatus(status);

        clienteService.editar(cliente);
    }

    public void excluir(int id) {
        clienteService.excluir(id);
    }

    public Cliente buscarPorId(int id) {
        return clienteService.buscarPorId(id);
    }

    public List<Cliente> listar() {
        return clienteService.listar();
    }
}