package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

    // Migrar = vai rodar as migrations, depois fecha tudo no final
    public static void migrar() {
        Flyway flyway = Flyway.configure()
                // Lembrar de alterar para sua senha e sua porta, pelo amor de deus, persistence.xml tbm
                .dataSource("jdbc:postgresql://localhost:3006/detetizadora_master", "postgres", "asdwsad")
                .load();

        flyway.migrate();  // Roda as migrations
    }
}