package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ServicoRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos servicos

public class ServicoService {
    // Gerencia as regras de negocio para Servico
    // Faz a ligacao entre a camada de view/controllers e o repository
    private final EntityManager em;
    private final ServicoRepository repository;

    // Recebe o EntityManager e cria o repository
    public ServicoService(EntityManager em) {
        this.em = em;
        this.repository = new ServicoRepository(em);
    }

    // Cria um novo servico e salva usando as validacoes deste service
    public Servico cadastrar(String nomeServico, String descricao, LocalDate data,
                             double valor, Cliente clienteEntity, Status status) {
        Servico servico = new Servico(nomeServico, descricao, data, valor, clienteEntity, status);
        salvar(servico);
        return servico;
    }

    // Salva um servico no banco com validacoes basicas
    public void salvar(Servico servico) {
        try {
            em.getTransaction().begin();

            // Validacoes simples antes de persistir
            if (servico.getValor() <= 0) {
                throw new NegocioException("Valor do servico deve ser maior que 0");
            }
            if (servico.getCliente() == null) {
                throw new NegocioException("Servico deve ter um cliente vinculado");
            }
            if (servico.getData() == null) {
                throw new NegocioException("Data do servico nao pode estar vazia");
            }

            repository.salvar(servico);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Regra de negocio invalida: desfaz transacao e propaga a excecao
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            // Erro generico: desfaz transacao e encapsula
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar servico: ", e);
        }
    }

    // Atualiza um servico existente aplicando validacoes
    public void editar(Servico servico) {
        try {
            em.getTransaction().begin();

            // Verifica se o servico existe antes de alterar
            Servico existente = repository.buscarPorId(servico.getId());
            if (existente == null) {
                throw new NegocioException("Servico nao encontrado");
            }
            if (servico.getValor() <= 0) {
                throw new NegocioException("Valor do servico deve ser maior que 0");
            }

            // Copia os dados atualizados
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
            throw new RuntimeException("Erro ao editar servico: ", e);
        }
    }

    // Remove um servico por id (apaga do banco)
    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            Servico servico = repository.buscarPorId(id);
            if (servico == null) {
                throw new NegocioException("Servico nao encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao excluir servico: ", e);
        }
    }

    // Busca um servico pelo id
    public Servico buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar servico: ", e);
        }
    }

    // Lista todos os servicos cadastrados
    public List<Servico> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar servicos: ", e);
        }
    }
}