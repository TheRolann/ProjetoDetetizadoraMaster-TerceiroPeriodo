package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {

    // Migrar = vai rodar as migrations, depois fecha tudo no final
    public static void migrar() {
        Flyway flyway = Flyway.configure()
                // Conecta ao PostgreSQL na porta 3006 com credenciais corretas
                .dataSource("jdbc:postgresql://localhost:3006/detetizadora_master", "postgres", "asdwsad")
                // Desabilita validação rigorosa
                .validateOnMigrate(false)
                // Permite executar migracoes mesmo modificadas
                .outOfOrder(true)
                .load();

        // Faz repair para atualizar checksums das migrations alteradas
        flyway.repair();

        // Roda as migrations
        flyway.migrate();
    }
}