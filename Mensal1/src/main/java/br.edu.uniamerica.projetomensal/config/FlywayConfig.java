package br.edu.uniamerica.projetomensal.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Classe de Configuração do Flyway
// Responsavel por inicializar e executar as migrations do banco de dados

@Configuration // Avisa que e configuracao
public class FlywayConfig {

     // @Bean que cria e configura o Flyway
     // @param dataSource - Conexao com o banco (vem do application.properties)
     // @return Flyway configurado e pronto para rodar

    @Bean
    public Flyway flyway(DataSource dataSource) {
        // Configura o Flyway com a origem dos scripts
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)        // Usa a conexao do DataSource
                .load();                       // Carrega os scripts

        // Roda as migrations automaticamente
        flyway.migrate();

        return flyway;
    }
}