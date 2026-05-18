package br.edu.uniamerica.projetomensal.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Classe responsavel por abrir e fechar a conexao com o banco usando JPA
public class PersistenceManager {
    // Fabrica que cria os EntityManager
    private static EntityManagerFactory emf;
    // Objeto usado para fazer operacoes no banco
    private static EntityManager em;

    // Abre a conexao com o banco quando a aplicacao inicia
    // Usa o nome da unidade de persistencia definido no persistence.xml
    public static void conectar() {
        emf = Persistence.createEntityManagerFactory("detetizadora_master");
        em = emf.createEntityManager();
    }

    // Entrega o EntityManager para outras classes usarem
    public static EntityManager getEntityManager() {
        return em;
    }

    // Fecha a conexao quando a aplicacao vai encerrar
    public static void desconectar() {
        emf.close();
        em.close();
    }

}
