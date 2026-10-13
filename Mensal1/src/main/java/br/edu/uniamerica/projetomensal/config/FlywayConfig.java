package br.edu.uniamerica.projetomensal.config;

import org.flywaydb.core.Flyway;

public class FlywayConfig {
    public static void migrar() {
        Flyway.configure()
                .dataSource(BancoConfig.url(), BancoConfig.usuario(), BancoConfig.senha())
                .load().migrate();
    }
}
