package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.view.panels.*;
import br.edu.uniamerica.projetomensal.model.Funcionario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTabbedPane abas;

    public MainFrame(Funcionario funcionario) {
        configurarJanela();
        inicializarAbas();
        adicionarRodape(funcionario);
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
        abas.addTab("Funcionarios", new FuncionarioPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Servicos",    new ServicoPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Relatorios",  new RelatorioPanel(PersistenceManager.getEntityManager()));

        add(abas, BorderLayout.CENTER);
    }

    private void adicionarRodape(Funcionario funcionario) {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Tema.COR_FUNDO);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.COR_BORDA),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        JLabel labelUsuario = new JLabel("Logado como: " + funcionario.getNome() + " - " + funcionario.getCargo());

        labelUsuario.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        labelUsuario.setFont(Tema.FONTE_REGULAR);

        rodape.add(labelUsuario, BorderLayout.WEST);
        add(rodape, BorderLayout.SOUTH);
    }
}