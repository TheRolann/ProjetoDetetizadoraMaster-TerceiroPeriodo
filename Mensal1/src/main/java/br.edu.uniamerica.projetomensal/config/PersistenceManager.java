package br.edu.uniamerica.projetomensal.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    // Ao iniciar a aplicacao, e chamado para criar a conexao com o banco de dados
    // utilizando o nome da unidade de persistencia definida no arquivo persistence.xml
    public static void conectar() {
        emf = Persistence.createEntityManagerFactory("detetizadora_master");
        em = emf.createEntityManager();
    }

    // Qualquer classe pode pegar o EntityManager
    public static EntityManager getEntityManager() {
        return em;
    }

    // Ao sair
    public static void desconectar() {
        emf.close();
        em.close();
    }

}
