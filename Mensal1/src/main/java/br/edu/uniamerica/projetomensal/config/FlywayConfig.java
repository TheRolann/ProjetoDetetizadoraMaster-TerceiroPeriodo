package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

// Classe responsavel por configurar e executar as migracoes do banco
public class FlywayConfig {

    // Metodo que inicia a atualizacao do banco com os scripts do Flyway
    public static void migrar() {
        // Pega os dados de conexao que estao nas variaveis de ambiente
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Fallback se a variavel de ambiente nao estiver definida
        // if (url == null) url = "jdbc:postgresql://localhost:5432/detetizadora_master";
        // if (user == null) user = "postgres";
        // if (password == null) password = "";

        // Cria o objeto Flyway com os dados do banco
        Flyway flyway = Flyway.configure()
                .dataSource(url, user, password)
                .validateOnMigrate(false)
                .outOfOrder(true)
                .load();

        // Corrige o historico antes de aplicar novas migracoes
        flyway.repair();

        // Executa as migracoes pendentes do banco
        flyway.migrate();
    }
}