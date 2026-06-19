package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

// Classe responsavel por configurar e executar as migracoes do banco
public class FlywayConfig {

    public static void migrar() {
        // Pega os dados de conexao das variaveis de ambiente.
        // NAO colocar senha de banco direto no codigo-fonte (risco de seguranca,
        // sobretudo se o repositorio for publico ou compartilhado).
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null) url = "jdbc:postgresql://localhost:5433/detetizadora_master";
        if (user == null) user = "postgres";

        if (password == null) {
            throw new IllegalStateException(
                    "Variavel de ambiente DB_PASSWORD nao definida. " +
                            "Configure-a antes de iniciar a aplicacao (nao deixe a senha no codigo)."
            );
        }

        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .validateOnMigrate(false)
                .outOfOrder(true)
                .load();

        flyway.repair();
        flyway.migrate();
    }
}
