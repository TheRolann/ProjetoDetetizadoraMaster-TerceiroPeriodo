package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ClienteRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos clientes

public class ClienteService {

    private EntityManager em;
    private ClienteRepository repository;

    public ClienteService(EntityManager em) {
        this.em = em;
        this.repository = new ClienteRepository(em);
    }

    // Metodo para cadastrar cliente
    public Cliente cadastrarCliente(String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
        Cliente cliente = new Cliente(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(cliente);
        return cliente;
    }

    // Metodos CRUD com transações

    public void salvar(Cliente cliente) {
        try {
            em.getTransaction().begin();

            // Validação de documento válido (CPF/CNPJ)
            if (cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
                throw new RuntimeException("Documento não pode estar vazio");
            }
            if (!cliente.getDocumento().matches("\\d{11}|\\d{14}")) {
                throw new RuntimeException("Documento deve conter exatamente 11 ou 14 dígitos");
            }

            repository.salvar(cliente);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void editar(Cliente cliente) {
        try {
            em.getTransaction().begin();

            if (cliente.getId() == 0) {
                throw new RuntimeException("ID não pode ser zero para edição");
            }

            Cliente existente = repository.buscarPorId(cliente.getId());
            if (existente == null) {
                throw new RuntimeException("Cliente não encontrado");
            }

            existente.setNomeEmpresa(cliente.getNomeEmpresa());
            existente.setDocumento(cliente.getDocumento());
            existente.setEndereco(cliente.getEndereco());
            existente.setTelefone(cliente.getTelefone());
            existente.setEmail(cliente.getEmail());
            existente.setStatus(cliente.getStatus());

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

            Cliente cliente = repository.buscarPorId(id);
            if (cliente == null) {
                throw new RuntimeException("Cliente não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Cliente buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Cliente> listar() {
        return repository.listar();
    }
}
