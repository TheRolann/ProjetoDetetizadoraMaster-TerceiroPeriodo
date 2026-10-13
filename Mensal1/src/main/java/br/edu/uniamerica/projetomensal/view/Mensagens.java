package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.utils.NegocioException;
import java.sql.SQLException;

public final class Mensagens {
    private Mensagens() {}
    public static String erro(Throwable erro) {
        for (Throwable causa = erro; causa != null; causa = causa.getCause()) {
            if (causa instanceof NegocioException) return causa.getMessage();
            if (causa instanceof SQLException sql) {
                if ("23503".equals(sql.getSQLState())) return "Registro possui vínculos. Use a inativação para preservar o histórico.";
                if ("23505".equals(sql.getSQLState())) return "Já existe um cadastro com esses dados. Confira documento ou horário.";
            }
        }
        erro.printStackTrace();
        return "Não foi possível concluir. Confira a conexão com o banco e tente novamente.";
    }
}
