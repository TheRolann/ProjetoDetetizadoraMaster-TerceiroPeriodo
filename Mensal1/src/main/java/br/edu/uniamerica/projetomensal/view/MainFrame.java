package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.view.panels.*;
import br.edu.uniamerica.projetomensal.model.Funcionario;

import javax.swing.*;
import java.awt.*;

// Janela principal do sistema depois do login
// Reune as telas de clientes, funcionarios, servicos e relatorios em abas
public class MainFrame extends JFrame {

    // Controle das abas da janela principal
    private JTabbedPane abas;

    // Construtor recebe o funcionario logado para mostrar no rodape
    public MainFrame(Funcionario funcionario) {
        configurarJanela();
        inicializarAbas();
        adicionarRodape(funcionario);
    }

    // Configura tamanho, titulo e layout da janela principal
    private void configurarJanela() {
        setTitle("Sistema Detetizadora Master");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());
    }

    // Cria as abas e adiciona cada painel do sistema
    private void inicializarAbas() {
        abas = new JTabbedPane();

        // Passa o EntityManager para cada painel via PersistenceManager
        // Cada aba recebe um painel independente
        abas.addTab("Clientes",    new ClientePanel(PersistenceManager.getEntityManager()));
        abas.addTab("Funcionarios", new FuncionarioPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Servicos",    new ServicoPanel(PersistenceManager.getEntityManager()));
        abas.addTab("Relatorios",  new RelatorioPanel(PersistenceManager.getEntityManager()));

        add(abas, BorderLayout.CENTER);
    }

    // Adiciona um rodape mostrando quem esta logado
    private void adicionarRodape(Funcionario funcionario) {
        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setBackground(Tema.COR_FUNDO);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.COR_BORDA),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        JLabel labelUsuario = new JLabel("Logado como: " + funcionario.getNome() + " - " + funcionario.getCargo());

        // Mostra nome e cargo do usuario autenticado
        labelUsuario.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        labelUsuario.setFont(Tema.FONTE_REGULAR);

        rodape.add(labelUsuario, BorderLayout.WEST);
        add(rodape, BorderLayout.SOUTH);
    }
}