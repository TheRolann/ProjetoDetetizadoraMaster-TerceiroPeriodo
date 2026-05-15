package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

    // Migrar = vai rodar as migrations, depois fecha tudo no final
    public static void migrar() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Fallback se variavel de ambiente nao estiver definida
        // if (url == null) url = "jdbc:postgresql://localhost:5432/detetizadora_master";
        // if (user == null) user = "postgres";
        // if (password == null) password = "";

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .validateOnMigrate(false)
                .outOfOrder(true)
                .load();

        // Faz repair para atualizar checksums das migrations alteradas
        flyway.repair();

        // Roda as migrations
        flyway.migrate();
    }
}