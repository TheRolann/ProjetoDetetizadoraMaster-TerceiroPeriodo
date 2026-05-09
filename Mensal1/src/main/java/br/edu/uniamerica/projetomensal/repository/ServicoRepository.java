package br.edu.uniamerica.projetomensal.repository;

import java.util.List;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import jakarta.persistence.EntityManager;

// Classe repository, que implementa a interface Crud, e é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) para a entidade Servico
public class ServicoRepository {

    private EntityManager em = PersistenceManager.getEntityManager();

    public void salvar(Servico servico) {
        em.getTransaction().begin(); // Inicia a operacao
        em.persist(servico);         // Salva no banco
        em.getTransaction().commit();// Finaliza a operacao
    }

    // Apaga o servico da lista, caso o id seja encontrado, pelo numero do id, utilizando o metodo buscarPorId para encontrar
    public void excluir(int id) {
        Servico servico = buscarPorId(id);
        if (servico != null) {
            em.getTransaction().begin();
            em.remove(servico);           // Remove do banco
            em.getTransaction().commit();
        }
    }

    public void editar(Servico servico) {
        em.getTransaction().begin();
        em.merge(servico);           // Atualiza no banco
        em.getTransaction().commit();
    }

    // Busca o servico na lista por id com for each, caso nao tenha, retorna null
    public Servico buscarPorId(int id) {
        return em.find(Servico.class, id); // Procura na classe Servico, o id passado como parametro
    }

    // Lista todos os servicos da lista, utilizando o metodo listar para retornar a lista de servicos
    public List<Servico> listar() {
        return em.createQuery("SELECT s FROM Servico s", Servico.class).getResultList();
    }
}
