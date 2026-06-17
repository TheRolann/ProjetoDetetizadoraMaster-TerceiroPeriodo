package br.edu.uniamerica.projetomensal.model.repository;

import br.edu.uniamerica.projetomensal.model.entity.ServicoEntity;
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
    public void salvar(ServicoEntity servicoEntity) {
        em.persist(servicoEntity);
    }

    // Exclui um servico pelo id
    public void excluir(int id) {
        ServicoEntity servicoEntity = buscarPorId(id);
        if (servicoEntity != null) {
            em.remove(servicoEntity);
        }
    }

    // Atualiza os dados de um servico existente
    public void editar(ServicoEntity servicoEntity) {
        em.merge(servicoEntity);
    }

    // Busca um servico pelo id
    public ServicoEntity buscarPorId(int id) {
        return em.find(ServicoEntity.class, id);
    }

    // Lista todos os servicos cadastrados
    public List<ServicoEntity> listar() {
        return em.createQuery("SELECT s FROM Servico s", ServicoEntity.class).getResultList();
    }
}