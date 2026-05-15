package br.edu.uniamerica.projetomensal.view;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private FuncionarioService funcionarioService;

    private JTextField campoNome;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JLabel labelErro;

    public LoginFrame() {
        this.funcionarioService = new FuncionarioService(PersistenceManager.getEntityManager());
        configurarJanela();
        inicializarComponentes();
    }

    private void configurarJanela() {
        setTitle("Login - Detetizadora Master");
        setSize(380, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Tema.COR_FUNDO);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {

        // ============ Titulo ============
        JLabel titulo = new JLabel("Detetizadora Master", SwingConstants.CENTER);
        titulo.setFont(Tema.FONTE_TITULO);
        titulo.setForeground(Tema.COR_DESTAQUE);
        titulo.setBorder(BorderFactory.createEmptyBorder(24, 0, 8, 0));
        add(titulo, BorderLayout.NORTH);

        // ============ Formulario ============
        JPanel painelForm = new JPanel(new GridLayout(4, 1, 0, 10));
        painelForm.setBackground(Tema.COR_FUNDO);
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // ============ Campo Nome ============
        JLabel labelNome = new JLabel("Nome de usuario:");
        labelNome.setForeground(Tema.COR_TEXTO);
        labelNome.setFont(Tema.FONTE_REGULAR);
        painelForm.add(labelNome);

        campoNome = new JTextField();
        EstiloUtils.estilizarCampo(campoNome);
        painelForm.add(campoNome);

        // ============ Campo Senha ============
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setForeground(Tema.COR_TEXTO);
        labelSenha.setFont(Tema.FONTE_REGULAR);
        painelForm.add(labelSenha);

        campoSenha = new JPasswordField();
        campoSenha.setBackground(Tema.COR_FUNDO_CAMPO);
        campoSenha.setForeground(Tema.COR_TEXTO);
        campoSenha.setCaretColor(Tema.COR_TEXTO);
        campoSenha.setFont(Tema.FONTE_REGULAR);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        painelForm.add(campoSenha);

        add(painelForm, BorderLayout.CENTER);

        // ============ Rodape ============
        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.setBackground(Tema.COR_FUNDO);
        painelRodape.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));

        // ============ Label Erro ============
        labelErro = new JLabel(" ", SwingConstants.CENTER);
        labelErro.setForeground(Tema.COR_DESTAQUE);
        labelErro.setFont(Tema.FONTE_REGULAR);
        painelRodape.add(labelErro, BorderLayout.NORTH);

        // ============ Botao entrar ============
        botaoEntrar = new JButton("Entrar");
        Tema.estilizarBotao(botaoEntrar);
        painelRodape.add(botaoEntrar, BorderLayout.CENTER);

        add(painelRodape, BorderLayout.SOUTH);

        // ============ Eventos ============
        botaoEntrar.addActionListener(e -> realizarLogin());

        // Enter no campo senha tambem loga
        campoSenha.addActionListener(e -> realizarLogin());
        campoNome.addActionListener(e -> campoSenha.requestFocus());

    }

    private void realizarLogin() {
        String nome = campoNome.getText().trim();
        String senha = new String(campoSenha.getText()).trim();

        if (nome.isEmpty() || senha.isEmpty()) {
            labelErro.setText("Preencha todos os campos.");
            return;
        }

        Funcionario funcionario = funcionarioService.autenticar(nome, senha);

        if (funcionario != null) {
            abrirSistema(funcionario);
        } else {
            labelErro.setText("Usuario ou senha incorreto");
            campoSenha.setText("");
        }
    }

    private void abrirSistema(Funcionario funcionario) {
        MainFrame mainFrame = new MainFrame(funcionario);
        mainFrame.setVisible(true);
        dispose(); // Fecha o login
    }

}
