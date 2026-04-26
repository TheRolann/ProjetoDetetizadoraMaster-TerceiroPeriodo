package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.repository.ServicoRepository;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos servicos

public class ServicoService {

    private EntityManager em;
    private ServicoRepository repository;

    public ServicoService(EntityManager em) {
        this.em = em;
        this.repository = new ServicoRepository(em);
    }

    // Metodo para cadastrar servico
    public Servico cadastrar(String nomeServico, String descricao, LocalDate data, double valor, Cliente cliente, Status status) {
        Servico servico = new Servico(nomeServico, descricao, data, valor, cliente, status);
        salvar(servico);
        return servico;
    }

    // Metodos CRUD com transações

    public void salvar(Servico servico) {
        try {
            em.getTransaction().begin();

            // Validação do valor do serviço
            if (servico.getValor() <= 0) {
                throw new NegocioException("O valor do serviço deve ser maior que 0");
            }

            // Validação de cliente
            if (servico.getCliente() == null) {
                throw new NegocioException("Serviço deve ter um cliente vinculado");
            }

            // Validação de data
            if (servico.getData() == null) {
                throw new NegocioException("Data do serviço não pode estar vazia");
            }

            repository.salvar(servico);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro inesperado ao salvar servico ", e);
        }
    }

    public void editar(Servico servico) {
        try {
            em.getTransaction().begin();

            if (servico.getId() == 0) {
                throw new NegocioException("ID não pode ser zero para edição");
            }

            Servico existente = repository.buscarPorId(servico.getId());
            if (existente == null) {
                throw new NegocioException("Serviço não encontrado");
            }

            // Validação do valor
            if (servico.getValor() <= 0) {
                throw new NegocioException("O valor do serviço deve ser maior que 0");
            }

            // Validação de cliente
            if (servico.getCliente() == null) {
                throw new NegocioException("Serviço deve ter um cliente vinculado");
            }

            existente.setNomeServico(servico.getNomeServico());
            existente.setDescricao(servico.getDescricao());
            existente.setData(servico.getData());
            existente.setValor(servico.getValor());
            existente.setCliente(servico.getCliente());
            existente.setStatus(servico.getStatus());

            repository.editar(existente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro inesperado ao editar servico ", e);
        }
    }

    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Servico servico = repository.buscarPorId(id);
            if (servico == null) {
                throw new NegocioException("Serviço não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro inesperado ao excluir servico ", e);
        }
    }

    public Servico buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao buscar servico por ID ", e);
        }
    }

    public List<Servico> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao listar servico ", e);
        }
    }
}