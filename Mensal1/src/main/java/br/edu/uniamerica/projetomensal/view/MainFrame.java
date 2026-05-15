package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.view.panels.ClientePanel;
import br.edu.uniamerica.projetomensal.view.panels.RelatorioPanel;
import br.edu.uniamerica.projetomensal.view.panels.ServicoPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane abas;

    public MainFrame() {
        configurarJanela();
        inicializarAbas();
    }

    private void configurarJanela() {
        setTitle("Sistema Detetizadora Master");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());
    }

    private void inicializarAbas() {
        abas = new JTabbedPane();

        // Passa o EntityManager para cada painel via PersistenceManager
        abas.addTab("Clientes",    new ClientePanel(PersistenceManager.getEntityManager()));
//        abas.addTab("Funcionários", new FuncionarioPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Serviços",    new ServicoPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Relatórios",  new RelatorioPanel(PersistenceManager.getEntityManager()));

        add(abas, BorderLayout.CENTER);
    }
}