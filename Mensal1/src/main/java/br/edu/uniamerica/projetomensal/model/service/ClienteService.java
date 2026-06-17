package br.edu.uniamerica.projetomensal.model.service;

import br.edu.uniamerica.projetomensal.model.entity.ClienteEntity;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.model.repository.ClienteRepository;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import br.edu.uniamerica.projetomensal.utils.ValidacaoDocumentos;
import jakarta.persistence.EntityManager;
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
    public ClienteEntity cadastrarCliente(String nomeEmpresa, String documento, String endereco,
                                          String telefone, String email, Status status) {
        ClienteEntity clienteEntity = new ClienteEntity(nomeEmpresa, documento, endereco, telefone, email, status);
        salvar(clienteEntity);
        return clienteEntity;
    }

    // Salva um cliente no banco com validacoes basicas
    public void salvar(ClienteEntity clienteEntity) {
        try {
            em.getTransaction().begin();

            // Documento nao pode ficar vazio
            if (clienteEntity.getDocumento() == null || clienteEntity.getDocumento().isEmpty()) {
                throw new NegocioException("Documento nao pode estar vazio");
            }
            // Documento precisa ter 11 ou 14 digitos
            if (!clienteEntity.getDocumento().matches("\\d{11}|\\d{14}")) {
                throw new NegocioException("Documento deve conter 11 ou 14 digitos");
            }
            if (!ValidacaoDocumentos.validar(clienteEntity.getDocumento())) {
                throw new NegocioException("CPF ou CNPJ invalido");
            }

            repository.salvar(clienteEntity);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Se der erro de regra de negocio, desfaz a transacao
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            // Se der qualquer outro erro, tambem desfaz a transacao
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar cliente: ", e);
        }
    }

    // Atualiza um cliente ja existente
    public void editar(ClienteEntity clienteEntity) {
        try {
            em.getTransaction().begin();

            // Primeiro verifica se o cliente existe
            ClienteEntity existente = repository.buscarPorId(clienteEntity.getId());
            if (existente == null) {
                throw new NegocioException("Cliente não encontrado");
            }

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
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            // Se acontecer outro erro, desfaz tudo tambem
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao editar cliente: ", e);
        }
    }

    // Exclui um cliente pelo id
    public void excluir(int id) {
        try {
            em.getTransaction().begin();

            // Confere se o cliente existe antes de apagar
            ClienteEntity clienteEntity = repository.buscarPorId(id);
            if (clienteEntity == null) {
                throw new NegocioException("Cliente não encontrado");
            }

            repository.excluir(id);
            em.getTransaction().commit();

        } catch (NegocioException e) {
            // Desfaz a operacao se o cliente nao existir
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            // Desfaz a operacao se ocorrer qualquer erro
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao excluir cliente: ", e);
        }
    }

    // Busca um cliente pelo id
    public ClienteEntity buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente: ", e);
        }
    }

    // Lista todos os clientes cadastrados
    public List<ClienteEntity> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes: ", e);
        }
    }
}