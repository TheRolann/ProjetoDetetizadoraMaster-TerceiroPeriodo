package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.repository.ServicoRepository;
import br.edu.uniamerica.projetomensal.model.enums.Status;
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
                throw new RuntimeException("O valor do serviço deve ser maior que 0");
            }

            // Validação de cliente
            if (servico.getCliente() == null) {
                throw new RuntimeException("Serviço deve ter um cliente vinculado");
            }

            // Validação de data
            if (servico.getData() == null) {
                throw new RuntimeException("Data do serviço não pode estar vazia");
            }

            repository.salvar(servico);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public void editar(Servico servico) {
        try {
            em.getTransaction().begin();

            if (servico.getId() == 0) {
                throw new RuntimeException("ID não pode ser zero para edição");
            }

            Servico existente = repository.buscarPorId(servico.getId());
            if (existente == null) {
                throw new RuntimeException("Serviço não encontrado");
            }

            // Validação do valor
            if (servico.getValor() <= 0) {
                throw new RuntimeException("O valor do serviço deve ser maior que 0");
            }

            // Validação de cliente
            if (servico.getCliente() == null) {
                throw new RuntimeException("Serviço deve ter um cliente vinculado");
            }

            existente.setNomeServico(servico.getNomeServico());
            existente.setDescricao(servico.getDescricao());
            existente.setData(servico.getData());
            existente.setValor(servico.getValor());
            existente.setCliente(servico.getCliente());
            existente.setStatus(servico.getStatus());

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

            Servico servico = repository.buscarPorId(id);
            if (servico == null) {
                throw new RuntimeException("Serviço não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Servico buscarPorId(int id) {
        return repository.buscarPorId(id);
    }

    public List<Servico> listar() {
        return repository.listar();
    }
}