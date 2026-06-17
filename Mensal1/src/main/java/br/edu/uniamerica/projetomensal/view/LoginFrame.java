package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.controller.FuncionarioController;
import br.edu.uniamerica.projetomensal.model.Funcionario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Tela inicial da aplicacao onde o usuario faz login
// Se as credenciais estiverem corretas, abre o sistema principal
public class LoginFrame extends JFrame {

    // Controller usado para autenticar o funcionario
    private FuncionarioController funcionarioController;

    // Campo do nome do usuario
    private JTextField campoNome;
    // Campo da senha do usuario
    private JPasswordField campoSenha;
    // Checkbox para mostrar ou esconder a senha
    private JCheckBox mostrarSenhaCheckBox;
    // Area que mostra mensagens para o usuario
    private JLabel labelMensagem;

    // Construtor da tela de login
    public LoginFrame() {
        this.funcionarioController = new FuncionarioController(PersistenceManager.getEntityManager());
        configurarJanela();
        inicializarComponentes();
    }

    // Configura tamanho, titulo e comportamento da janela
    private void configurarJanela() {
        setTitle("Login — Detetizadora Master");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Tema.COR_FUNDO);
    }

    // Monta todos os componentes visuais da tela de login
    private void inicializarComponentes() {

        // ========== PAINEL PRINCIPAL ==========
        JPanel painel = new JPanel();
        painel.setLayout(new GridBagLayout());
        painel.setBackground(Tema.COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // ========== TITULO ==========
        JLabel titulo = new JLabel("Detetizadora Master", SwingConstants.CENTER);
        titulo.setFont(Tema.FONTE_TITULO);
        titulo.setForeground(Tema.COR_DESTAQUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 8, 10);
        painel.add(titulo, gbc);

        // ========== SUBTITULO ==========
        JLabel subtitulo = new JLabel("🦟 Sistema de Controle de Pragas", SwingConstants.CENTER);
        subtitulo.setFont(Tema.FONTE_REGULAR);
        subtitulo.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 25, 10);
        painel.add(subtitulo, gbc);

        // ========== LABEL USUARIO ==========
        JLabel labelNome = new JLabel("Usuário:");
        labelNome.setFont(Tema.FONTE_BOLD);
        labelNome.setForeground(Tema.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 4, 10);
        painel.add(labelNome, gbc);

        // ========== CAMPO USUARIO ==========
        campoNome = new JTextField(18);
        EstiloUtils.estilizarCampo(campoNome);
        gbc.gridx = 1;
        gbc.gridy = 2;
        painel.add(campoNome, gbc);

        // ========== LABEL SENHA ==========
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(Tema.FONTE_BOLD);
        labelSenha.setForeground(Tema.COR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 10, 4, 10);
        painel.add(labelSenha, gbc);

        // ========== CAMPO SENHA ==========
        campoSenha = new JPasswordField(18);
        campoSenha.setBackground(Tema.COR_FUNDO_CAMPO);
        campoSenha.setForeground(Tema.COR_TEXTO);
        campoSenha.setCaretColor(Tema.COR_TEXTO);
        campoSenha.setFont(Tema.FONTE_REGULAR);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 3;
        painel.add(campoSenha, gbc);

        // ========== CHECKBOX MOSTRAR SENHA ==========
        mostrarSenhaCheckBox = new JCheckBox("Mostrar senha");
        mostrarSenhaCheckBox.setFont(Tema.FONTE_REGULAR);
        mostrarSenhaCheckBox.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        mostrarSenhaCheckBox.setBackground(Tema.COR_FUNDO);
        mostrarSenhaCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mostrarSenhaCheckBox.setFocusPainted(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 10, 14, 10);
        painel.add(mostrarSenhaCheckBox, gbc);

        // ========== PAINEL BOTOES ==========
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        painelBotoes.setBackground(Tema.COR_FUNDO);

        JButton botaoEntrar = new JButton("Entrar");
        Tema.estilizarBotao(botaoEntrar);
        botaoEntrar.setPreferredSize(new Dimension(110, 36));

        JButton botaoLimpar = new JButton("Limpar");
        botaoLimpar.setFont(Tema.FONTE_BOTAO);
        botaoLimpar.setBackground(Tema.COR_FUNDO_CAMPO);
        botaoLimpar.setForeground(Tema.COR_TEXTO);
        botaoLimpar.setFocusPainted(false);
        botaoLimpar.setBorderPainted(false);
        botaoLimpar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoLimpar.setPreferredSize(new Dimension(110, 36));
        botaoLimpar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { botaoLimpar.setBackground(Tema.COR_BORDA); }
            public void mouseExited(MouseEvent e)  { botaoLimpar.setBackground(Tema.COR_FUNDO_CAMPO); }
        });

        JButton botaoSair = new JButton("Sair");
        botaoSair.setFont(Tema.FONTE_BOTAO);
        botaoSair.setBackground(Tema.COR_FUNDO_CAMPO);
        botaoSair.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        botaoSair.setFocusPainted(false);
        botaoSair.setBorderPainted(false);
        botaoSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botaoSair.setPreferredSize(new Dimension(110, 36));
        botaoSair.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { botaoSair.setBackground(Tema.COR_BORDA); }
            public void mouseExited(MouseEvent e)  { botaoSair.setBackground(Tema.COR_FUNDO_CAMPO); }
        });

        painelBotoes.add(botaoEntrar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoSair);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 10, 16, 10);
        painel.add(painelBotoes, gbc);

        // ========== LABEL MENSAGEM ==========
        labelMensagem = new JLabel("Digite seu usuário e senha", SwingConstants.CENTER);
        labelMensagem.setFont(Tema.FONTE_REGULAR);
        labelMensagem.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 10, 0, 10);
        painel.add(labelMensagem, gbc);

        add(painel);

        // ========== EVENTOS ==========
        botaoEntrar.addActionListener(e -> realizarLogin());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoSair.addActionListener(e -> System.exit(0));

        campoNome.addActionListener(e -> campoSenha.requestFocus());
        campoSenha.addActionListener(e -> realizarLogin());

        mostrarSenhaCheckBox.addActionListener(e -> {
            if (mostrarSenhaCheckBox.isSelected()) {
                campoSenha.setEchoChar((char) 0);
            } else {
                campoSenha.setEchoChar('•');
            }
        });

        SwingUtilities.invokeLater(() -> campoNome.requestFocus());
    }

    // ========== METODOS DE ACAO ==========

    private void realizarLogin() {
        String nome  = campoNome.getText().trim();
        String senha = new String(campoSenha.getPassword()).trim();

        if (nome.isEmpty()) {
            mostrarMensagem("Por favor, digite o usuário.", true);
            campoNome.requestFocus();
            return;
        }
        if (senha.isEmpty()) {
            mostrarMensagem("Por favor, digite a senha.", true);
            campoSenha.requestFocus();
            return;
        }

        Funcionario funcionario = funcionarioController.autenticar(nome, senha);

        if (funcionario != null) {
            mostrarMensagem("Login realizado! Bem-vindo, " + funcionario.getNome() + ".", false);
            abrirSistema(funcionario);
        } else {
            mostrarMensagem("Usuário ou senha incorretos. Tente novamente.", true);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoSenha.setText("");
        mostrarSenhaCheckBox.setSelected(false);
        campoSenha.setEchoChar('•');
        mostrarMensagem("Campos limpos. Digite seus dados.", false);
        campoNome.requestFocus();
    }

    private void mostrarMensagem(String mensagem, boolean isErro) {
        labelMensagem.setText(mensagem);
        labelMensagem.setForeground(isErro ? Tema.COR_DESTAQUE : Tema.COR_TEXTO_SECUNDARIO);

        if (!isErro) {
            Timer timer = new Timer(3000, e -> {
                labelMensagem.setText("Digite seu usuário e senha");
                labelMensagem.setForeground(Tema.COR_TEXTO_SECUNDARIO);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void abrirSistema(Funcionario funcionario) {
        MainFrame mainFrame = new MainFrame(funcionario);
        mainFrame.setVisible(true);
        dispose();
    }
}