package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.model.Servico;
import jakarta.persistence.EntityManager;
import java.util.List;

// Classe responsavel por acessar e manipular servicos no banco
public class ServicoRepository {

    // Objeto que faz a comunicacao com o banco
    private final EntityManager em;

    // Recebe o EntityManager no construtor para usar nas operacoes
    public ServicoRepository(EntityManager em) {
        this.em = em;
    }

    // Salva um novo servico no banco
    public void salvar(Servico servico) {
        em.persist(servico);
    }

    // Exclui um servico pelo id
    public void excluir(int id) {
        Servico servico = buscarPorId(id);
        if (servico != null) {
            em.remove(servico);
        }
    }

    // Atualiza os dados de um servico existente
    public void editar(Servico servico) {
        em.merge(servico);
    }

    // Busca um servico pelo id
    public Servico buscarPorId(int id) {
        return em.find(Servico.class, id);
    }

    // Lista todos os servicos cadastrados
    public List<Servico> listar() {
        return em.createQuery("SELECT s FROM Servico s", Servico.class).getResultList();
    }
}