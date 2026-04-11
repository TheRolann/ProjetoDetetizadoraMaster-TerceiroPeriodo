package br.edu.uniamerica.projetomensal.repository;

import java.util.List;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import jakarta.persistence.EntityManager;

// Classe repository responsável por realizar as operações de CRUD para a entidade Servico
public class ServicoRepository {

    private EntityManager em;

    // Construtor que aceita EntityManager como parâmetro
    public ServicoRepository(EntityManager em) {
        this.em = em;
    }

    // Construtor padrão para compatibilidade
    public ServicoRepository() {
        this.em = PersistenceManager.getEntityManager();
    }

    // Salva o serviço no banco
    public void salvar(Servico servico) {
        em.persist(servico);
    }

    // Apaga o serviço do banco caso o id seja encontrado
    public void excluir(int id) {
        Servico servico = buscarPorId(id);
        if (servico != null) {
            em.remove(servico);
        }
    }

    // Atualiza o serviço no banco
    public void editar(Servico servico) {
        em.merge(servico);
    }

    // Busca o serviço no banco por id
    public Servico buscarPorId(int id) {
        return em.find(Servico.class, id);
    }

    // Lista todos os serviços do banco
    public List<Servico> listar() {
        return em.createQuery("SELECT s FROM Servico s", Servico.class).getResultList();
    }
}
