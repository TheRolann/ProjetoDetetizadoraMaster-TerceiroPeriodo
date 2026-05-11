package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.FlywayConfig;
import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 1 - Roda as migrations antes de tudo
        FlywayConfig.migrar();

        // 2 - Abre a conexao JPA
        PersistenceManager.conectar();

        // 3 - Inicia a janela Swing na thread correta
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}