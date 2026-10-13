package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.repository.ClienteRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import br.edu.uniamerica.projetomensal.utils.ValidacaoDocumentos;
import jakarta.persistence.EntityManager;
import br.edu.uniamerica.projetomensal.config.Sessao;
import br.edu.uniamerica.projetomensal.utils.ValidacaoCadastro;
import br.edu.uniamerica.projetomensal.utils.Transacoes;

import java.util.List;

// Classe que recebe as regras de negocio dos clientes
// Ela conversa com o repository e controla as transacoes
public class ClienteService {

    // Objeto usado para controlar a transacao com o banco
    private final EntityManager em;
    // Repository usado para acessar os dados do cliente
    private final ClienteRepository repository;

    // Recebe o EntityManager e cria o repository
    public ClienteService(EntityManager em) {
        this.em = em;
        this.repository = new ClienteRepository(em);
    }

    // Cria um cliente novo e salva usando os dados recebidos
    public Cliente cadastrarCliente(String nomeEmpresa, String documento, String endereco,
                                    String telefone, String email, Status status) {
        Cliente clienteEntity = new Cliente(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(clienteEntity);
        return clienteEntity;
    }

    // Salva um cliente no banco com validacoes basicas
    public void salvar(Cliente clienteEntity) {
        Sessao.exigirLogin();
        if (clienteEntity.getStatus() == Status.INATIVO) Sessao.exigirGerente();
        ValidacaoCadastro.cliente(clienteEntity);
        try {
            em.getTransaction().begin();

            verificarDocumento(clienteEntity);

            repository.salvar(clienteEntity);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Se der erro de regra de negocio, desfaz a transacao
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            // Se der qualquer outro erro, tambem desfaz a transacao
            Transacoes.desfazer(em);
            throw new RuntimeException("Erro ao salvar cliente: ", e);
        }
    }

    // Atualiza um cliente ja existente
    public void editar(Cliente clienteEntity) {
        Sessao.exigirLogin();
        if (clienteEntity.getStatus() == Status.INATIVO) Sessao.exigirGerente();
        ValidacaoCadastro.cliente(clienteEntity);
        try {
            em.getTransaction().begin();

            // Primeiro verifica se o cliente existe
            Cliente existente = repository.buscarPorId(clienteEntity.getId());
            if (existente == null) {
                throw new NegocioException("Cliente não encontrado");
            }

            verificarDocumento(clienteEntity);

            // Copia os novos dados para o objeto encontrado
            existente.setNomeEmpresa(clienteEntity.getNomeEmpresa());
            existente.setDocumento(clienteEntity.getDocumento());
            existente.setEndereco(clienteEntity.getEndereco());
            existente.setTelefone(clienteEntity.getTelefone());
            existente.setEmail(clienteEntity.getEmail());
            existente.setStatus(clienteEntity.getStatus());

            repository.editar(existente);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Se a regra de negocio falhar, desfaz tudo
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            // Se acontecer outro erro, desfaz tudo tambem
            Transacoes.desfazer(em);
            throw new RuntimeException("Erro ao editar cliente: ", e);
        }
    }

    private void verificarDocumento(Cliente cliente) {
        if (repository.documentoEmUso(cliente.getDocumento(), cliente.getId())) {
            throw new NegocioException("Documento já cadastrado para outro cliente.");
        }
    }

    // Exclui um cliente pelo id
    public void excluir(int id) {
        Sessao.exigirGerente();
        try {
            em.getTransaction().begin();

            // Confere se o cliente existe antes de apagar
            Cliente clienteEntity = repository.buscarPorId(id);
            if (clienteEntity == null) {
                throw new NegocioException("Cliente não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Desfaz a operacao se o cliente nao existir
            Transacoes.desfazer(em);
            throw e;
        } catch (Exception e) {
            // Desfaz a operacao se ocorrer qualquer erro
            Transacoes.desfazer(em);
            throw new RuntimeException("Erro ao excluir cliente: ", e);
        }
    }

    // Busca um cliente pelo id
    public Cliente buscarPorId(int id) {
        Sessao.exigirLogin();
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente: ", e);
        }
    }

    // Lista todos os clientes cadastrados
    public List<Cliente> listar() {
        Sessao.exigirLogin();
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes: ", e);
        }
    }
}