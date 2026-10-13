package br.edu.uniamerica.projetomensal.utils;

import jakarta.persistence.EntityManager;

public final class Transacoes {
    private Transacoes() {}

    public static void desfazer(EntityManager em) {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        // Evita reaproveitar alterações que foram rejeitadas.
        em.clear();
    }
}
