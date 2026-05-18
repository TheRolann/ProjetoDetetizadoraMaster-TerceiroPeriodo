package br.edu.uniamerica.projetomensal.view.panels.preview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class LoginPanel extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JLabel mensagemLabel;
    private HashMap<String, String> usuarios;
    private JCheckBox mostrarSenhaCheckBox;

    public LoginPanel() {
        // Inicializar banco de dados de usuários
        usuarios = new HashMap<>();
        usuarios.put("admin", "123");

        // Configurar a janela principal
        setTitle("Sistema de Login");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centralizar na tela
        setResizable(false);

        // Criar o painel principal com borda
        JPanel painel = new JPanel();
        painel.setLayout(new GridBagLayout());
        painel.setBackground(new Color(240, 240, 240));
        painel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Título
        JLabel tituloLabel = new JLabel("Bem-vindo ao Sistema");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 22));
        tituloLabel.setForeground(new Color(50, 50, 150));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 25, 10);
        painel.add(tituloLabel, gbc);

        // Usuário
        JLabel usuarioLabel = new JLabel("Usuário:");
        usuarioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        painel.add(usuarioLabel, gbc);

        campoUsuario = new JTextField(18);
        campoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        campoUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        painel.add(campoUsuario, gbc);

        // Senha
        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(senhaLabel, gbc);

        campoSenha = new JPasswordField(18);
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        painel.add(campoSenha, gbc);

        // CheckBox para mostrar senha
        mostrarSenhaCheckBox = new JCheckBox("Mostrar senha");
        mostrarSenhaCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        mostrarSenhaCheckBox.setBackground(new Color(240, 240, 240));
        mostrarSenhaCheckBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 10, 15, 10);
        painel.add(mostrarSenhaCheckBox, gbc);

        // Painel para botões (centralizado)
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        botoesPanel.setBackground(new Color(240, 240, 240));

        // Botão de login
        JButton loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(50, 150, 50));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(100, 35));
        botoesPanel.add(loginButton);

        // Botão de limpar
        JButton clearButton = new JButton("Limpar");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
        clearButton.setBackground(new Color(150, 150, 150));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setPreferredSize(new Dimension(100, 35));
        botoesPanel.add(clearButton);

        // Botão de sair
        JButton exitButton = new JButton("Sair");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setPreferredSize(new Dimension(100, 35));
        botoesPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        painel.add(botoesPanel, gbc);

        // Mensagem de status
        mensagemLabel = new JLabel("Digite seu usuário e senha");
        mensagemLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        mensagemLabel.setForeground(Color.GRAY);
        mensagemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 0, 10);
        painel.add(mensagemLabel, gbc);

        // Ação do CheckBox para mostrar/ocultar senha
        mostrarSenhaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mostrarSenhaCheckBox.isSelected()) {
                    campoSenha.setEchoChar((char) 0);
                } else {
                    campoSenha.setEchoChar('•');
                }
            }
        });

        // Adicionar ações aos botões
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Permitir login com Enter
        campoUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });

        campoSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });

        // Adicionar painel à janela
        add(painel);

        // Setar foco no campo de usuário
        campoUsuario.requestFocus();
    }

    private void realizarLogin() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());

        // Validação básica
        if (usuario.isEmpty()) {
            mostrarMensagem("Por favor, digite o usuário!", true);
            campoUsuario.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            mostrarMensagem("Por favor, digite a senha!", true);
            campoSenha.requestFocus();
            return;
        }

        // Verificar credenciais
        if (usuarios.containsKey(usuario) && usuarios.get(usuario).equals(senha)) {
            mostrarMensagem("Login realizado com sucesso!\nBem-vindo, " + usuario + "!", false);
            abrirPainelPrincipal(usuario);
        } else {
            mostrarMensagem("Usuário ou senha incorretos!\nTente novamente.", true);
            campoSenha.setText("");
            campoSenha.requestFocus();
        }
    }

    private void limparCampos() {
        campoUsuario.setText("");
        campoSenha.setText("");
        campoUsuario.requestFocus();
        mostrarMensagem("Campos limpos! Digite seus dados.", false);

        if (mostrarSenhaCheckBox.isSelected()) {
            mostrarSenhaCheckBox.setSelected(false);
            campoSenha.setEchoChar('•');
        }
    }

    private void mostrarMensagem(String mensagem, boolean isErro) {
        mensagemLabel.setText(mensagem);
        if (isErro) {
            mensagemLabel.setForeground(Color.RED);
        } else {
            mensagemLabel.setForeground(new Color(50, 150, 50));
        }

        if (!isErro && !mensagem.equals("Login realizado com sucesso!\nBem-vindo, " +
                campoUsuario.getText() + "!")) {
            Timer timer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!mensagemLabel.getText().contains("sucesso")) {
                        mensagemLabel.setText("Digite seu usuário e senha");
                        mensagemLabel.setForeground(Color.GRAY);
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void abrirPainelPrincipal(String usuario) {
        JFrame mainFrame = new JFrame("Painel Principal - " + usuario);
        mainFrame.setSize(500, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Bem-vindo(a), " + usuario + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel menuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton perfilButton = new JButton("Meu Perfil");
        perfilButton.setFont(new Font("Arial", Font.PLAIN, 14));
        perfilButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton logoutButton = new JButton("Sair do Sistema");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(200, 50, 50));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        menuPanel.add(perfilButton);
        menuPanel.add(logoutButton);

        perfilButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Nome: " + usuario + "\nEmail: " + usuario + "@exemplo.com\nStatus: Ativo",
                    "Perfil do Usuário", JOptionPane.INFORMATION_MESSAGE);
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(mainFrame,
                    "Deseja realmente sair do sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                setVisible(true);
                limparCampos();
            }
        });

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPanel().setVisible(true);
            }
        });
    }
}