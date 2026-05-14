package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ServicoRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class ServicoService {

    private EntityManager em;
    private ServicoRepository repository;

    public ServicoService(EntityManager em) {
        this.em = em;
        this.repository = new ServicoRepository(em);
    }

    public Servico cadastrar(String nomeServico, String descricao, LocalDate data,
                             double valor, Cliente cliente, Status status) {
        Servico servico = new Servico(nomeServico, descricao, data, valor, cliente, status);
        salvar(servico);
        return servico;
    }

    public void salvar(Servico servico) {
        try {
            em.getTransaction().begin();

            if (servico.getValor() <= 0) {
                throw new NegocioException("Valor do serviço deve ser maior que 0");
            }
            if (servico.getCliente() == null) {
                throw new NegocioException("Serviço deve ter um cliente vinculado");
            }
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
            throw new RuntimeException("Erro ao salvar serviço: ", e);
        }
    }

    public void editar(Servico servico) {
        try {
            em.getTransaction().begin();

            Servico existente = repository.buscarPorId(servico.getId());
            if (existente == null) {
                throw new NegocioException("Serviço não encontrado");
            }
            if (servico.getValor() <= 0) {
                throw new NegocioException("Valor do serviço deve ser maior que 0");
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
            throw new RuntimeException("Erro ao editar serviço: ", e);
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
            throw new RuntimeException("Erro ao excluir serviço: ", e);
        }
    }

    public Servico buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar serviço: ", e);
        }
    }

    public List<Servico> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar serviços: ", e);
        }
    }
}