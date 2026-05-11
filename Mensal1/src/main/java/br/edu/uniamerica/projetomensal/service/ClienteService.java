package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ClienteRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClienteService {

    private EntityManager em;
    private ClienteRepository repository;

    public ClienteService(EntityManager em) {
        this.em = em;
        this.repository = new ClienteRepository(em);
    }

    public Cliente cadastrarCliente(String nomeEmpresa, String documento, String endereco,
                                    String telefone, String email, Status status) {
        Cliente cliente = new Cliente(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(cliente);
        return cliente;
    }

    public void salvar(Cliente cliente) {
        try {
            em.getTransaction().begin();

            if (cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
                throw new NegocioException("Documento não pode estar vazio");
            }
            if (!cliente.getDocumento().matches("\\d{11}|\\d{14}")) {
                throw new NegocioException("Documento deve conter 11 ou 14 dígitos");
            }

            repository.salvar(cliente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar cliente: ", e);
        }
    }

    public void editar(Cliente cliente) {
        try {
            em.getTransaction().begin();

            Cliente existente = repository.buscarPorId(cliente.getId());
            if (existente == null) {
                throw new NegocioException("Cliente não encontrado");
            }

            existente.setNomeEmpresa(cliente.getNomeEmpresa());
            existente.setDocumento(cliente.getDocumento());
            existente.setEndereco(cliente.getEndereco());
            existente.setTelefone(cliente.getTelefone());
            existente.setEmail(cliente.getEmail());
            existente.setStatus(cliente.getStatus());

            repository.editar(existente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao editar cliente: ", e);
        }
    }

    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Cliente cliente = repository.buscarPorId(id);
            if (cliente == null) {
                throw new NegocioException("Cliente não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao excluir cliente: ", e);
        }
    }

    public Cliente buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente: ", e);
        }
    }

    public List<Cliente> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes: ", e);
        }
    }
}