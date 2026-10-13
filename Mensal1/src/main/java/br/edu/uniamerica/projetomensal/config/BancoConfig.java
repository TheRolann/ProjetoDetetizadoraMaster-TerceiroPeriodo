package br.edu.uniamerica.projetomensal.config;

import java.util.Map;

// Flyway e Hibernate usam exatamente a mesma conexão.
public final class BancoConfig {
    private BancoConfig() {}

    public static String url() {
        return ambiente("DB_URL", "jdbc:postgresql://localhost:5432/detetizadora_master");
    }

    public static String usuario() { return ambiente("DB_USER", "postgres"); }

    public static String senha() {
        String senha = System.getenv("DB_PASSWORD");
        if (senha == null || senha.isBlank()) {
            throw new IllegalStateException("Defina DB_PASSWORD antes de iniciar o sistema.");
        }
        return senha;
    }

    public static Map<String, Object> propriedadesJpa() {
        return Map.of("jakarta.persistence.jdbc.url", url(),
                "jakarta.persistence.jdbc.user", usuario(),
                "jakarta.persistence.jdbc.password", senha());
    }

    private static String ambiente(String nome, String padrao) {
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? padrao : valor;
    }
}
