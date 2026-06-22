package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.FlywayConfig;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.view.LoginFrame;
import br.edu.uniamerica.projetomensal.view.Tema;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Aplica o tema antes de qualquer componente Swing ser criado
        Tema.aplicar();

        // 1 - Roda as migrations antes de tudo
        FlywayConfig.migrar();

        // 2 - Abre a conexao JPA
        PersistenceManager.conectar();

        // 3 - Inicia a janela Swing na thread correta
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}