package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

    // Migrar = vai rodar as migrations, depois fecha tudo no final
    public static void migrar() {
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/detetizadora_master", "postgres", "132435")
                .load();

        flyway.migrate();
    }
}