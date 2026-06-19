package br.edu.uniamerica.projetomensal.model.repository;

import br.edu.uniamerica.projetomensal.model.entity.ClienteEntity;
import jakarta.persistence.EntityManager;

import java.util.List;

// Classe responsavel por acessar e manipular os dados de Cliente no banco
// Aqui ficam as operacoes basicas de persistencia usando JPA
public class ClienteRepository {

    // Objeto que faz a comunicacao com o banco de dados
    private final EntityManager em;

    // Recebe o EntityManager no construtor para reutilizar nas operacoes
    public ClienteRepository(EntityManager em) {
        this.em = em;
    }

    // Salva um novo cliente no banco
    public void salvar(ClienteEntity clienteEntity) {
        em.persist(clienteEntity); // Adiciona o objeto no contexto de persistencia
    }

    // Exclui um cliente pelo id
    // Primeiro busca o registro, depois remove se ele existir
    public void excluir(int id) {
        ClienteEntity clienteEntity = buscarPorId(id); // Busca o cliente pela chave primaria
        if (clienteEntity != null) {
            em.remove(clienteEntity); // Remove o registro do banco
        }
    }

    // Atualiza os dados de um cliente ja existente
    public void editar(ClienteEntity clienteEntity) {
        em.merge(clienteEntity); // Atualiza o objeto no banco
    }

    // Busca um cliente pelo id
    // Retorna null se nao encontrar
    public ClienteEntity buscarPorId(int id) {
        return em.find(ClienteEntity.class, id); // Procura pela chave primaria
    }

    // Lista todos os clientes cadastrados
    public List<ClienteEntity> listar() {
        return em.createQuery("SELECT c FROM Cliente c", ClienteEntity.class).getResultList();
    }
}