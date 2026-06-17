package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.controller.funcionario.FuncionarioController;
import br.edu.uniamerica.projetomensal.controller.funcionario.FuncionarioRequest;
import br.edu.uniamerica.projetomensal.controller.funcionario.FuncionarioResponse;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.utils.ValidacaoDocumentos;
import br.edu.uniamerica.projetomensal.view.EstiloUtils;
import br.edu.uniamerica.projetomensal.view.Tema;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Painel responsavel por gerenciar a tela de funcionarios
// Contem formulario, tabela e botoes para cadastro, edicao e exclusao
public class FuncionarioPanel extends JPanel {

    // Service que faz a ligacao com a camada de negocio
    private FuncionarioController funcionarioController;

    // === Campos do formulario =====
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JTextField campoSalario;
    private JComboBox<Cargo> campoCargo;
    private JComboBox<Status> campoStatus;
    private JPasswordField campoSenha;

    // === Tabela ======================
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // === Botoes ======================
    private JButton botaoSalvar;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;

    // Guarda o ID do funcionario selecionado para editar/excluir
    private int idSelecionado = -1;

    // Construtor do painel: recebe o EntityManager, cria o service
    // e monta a interface inicial com os dados da tabela
    public FuncionarioPanel(EntityManager em) {
        this.funcionarioController = new FuncionarioController(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

    // Monta toda a interface visual do painel
    // Divide a tela em formulario na esquerda e tabela na direita
    private void inicializarComponentes() {

        // ========== Lado Esquerdo - Formulario ==========
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        // === Titulo ======================
        JLabel titulo = new JLabel("Funcionários", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelEsquerdo.add(titulo, BorderLayout.NORTH);

        // === Campos ======================
        JPanel painelCampos = new JPanel(new GridLayout(9, 2, 5, 8));

        painelCampos.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("CPF:"));
        campoCpf = new JTextField();
        painelCampos.add(campoCpf);

        painelCampos.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField();
        painelCampos.add(campoTelefone);

        painelCampos.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelCampos.add(campoEmail);

        painelCampos.add(new JLabel("Endereco:"));
        campoEndereco = new JTextField();
        painelCampos.add(campoEndereco);

        painelCampos.add(new JLabel("Salario:"));
        campoSalario = new JTextField();
        painelCampos.add(campoSalario);

        painelCampos.add(new JLabel("Cargo:"));
        campoCargo = new JComboBox<>(Cargo.values());
        painelCampos.add(campoCargo);

        painelCampos.add(new JLabel("Status:"));
        campoStatus = new JComboBox<>(new Status[]{Status.ATIVO, Status.INATIVO});
        painelCampos.add(campoStatus);

        painelCampos.add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        campoSenha.setBackground(Tema.COR_FUNDO_CAMPO);
        campoSenha.setForeground(Tema.COR_TEXTO);
        campoSenha.setCaretColor(Tema.COR_TEXTO);
        campoSenha.setFont(Tema.FONTE_REGULAR);
        campoSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        painelCampos.add(campoSenha);

        painelEsquerdo.add(painelCampos, BorderLayout.CENTER);

        // === Botoes ======================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoSalvar = new JButton("Salvar");
        botaoEditar = new JButton("Editar");
        botaoExcluir = new JButton("Excluir");
        botaoLimpar = new JButton("Limpar");

        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoLimpar);
        painelEsquerdo.add(painelBotoes, BorderLayout.SOUTH);

        // ========== Lado Direito - Tabela ==========
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Lista de Funcionarios"));

        // === Colunas da tabela ======================
        String[] colunas = {"ID", "Nome", "CPF", "Cargo", "Salario", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override // Metodo da classe DefaultTableModel. Controla se uma celula da tabela pode ser editada diretamente
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela nao editavel diretamente
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(40); // Coluna ID pequena

        JScrollPane scroll = new JScrollPane(tabela);
        painelDireito.add(scroll, BorderLayout.CENTER);

        // ============== Divisao 50/50 =============
        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        divisor.setDividerLocation(420);
        divisor.setResizeWeight(0.45);

        add(divisor, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        // ========== Eventos ==========
        botaoSalvar.addActionListener(e -> salvarFuncionario());
        botaoEditar.addActionListener(e -> editarFuncionario());
        botaoExcluir.addActionListener(e -> excluirFuncionario());
        botaoLimpar.addActionListener(e -> limparFormulario());

        // Verifica linha valida antes de atualizar idSelecionado
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabela.getSelectedRow();
                if (linha >= 0) {
                    idSelecionado = (int) modeloTabela.getValueAt(linha, 0);
                    preencherFormularioComSelecionado();
                }
            }
        });
    }

    // ========== Metodos de Acao ==========

    // Salva um funcionario novo depois de validar os campos
    private void salvarFuncionario() {
        if (!validarCampos()) return;

        try {
            String cpf = campoCpf.getText().trim().replaceAll("[./-]", "");
            double salario = Double.parseDouble(campoSalario.getText().trim().replace(",", "."));
            String senha = new String(campoSenha.getPassword()).trim();
            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Senha é obrigatória.");
                return;
            }

            FuncionarioRequest req = new FuncionarioRequest(

                    campoNome.getText().trim(),
                    cpf,
                    senha,
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    campoEndereco.getText().trim(),
                    salario,
                    (Cargo) campoCargo.getSelectedItem(),
                    Status.ATIVO
                    );


            // Envia o objeto completo para o service persistir
            funcionarioController.salvarFuncionario(req);
            JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Salário invalido. Use numeros (ex: 2500.00).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edita o funcionario selecionado na tabela
    private void editarFuncionario() {

        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um funcionario na tabela para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            String cpf = campoCpf.getText().trim().replaceAll("[./-]", "");
            double salario = Double.parseDouble(campoSalario.getText().trim().replace(",", "."));
            String senha = new String(campoSenha.getPassword()).trim();

            FuncionarioRequest req = new FuncionarioRequest(
                    campoNome.getText().trim(),
                    cpf,
                    senha,
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    campoEndereco.getText().trim(),
                    salario,
                    (Cargo) campoCargo.getSelectedItem(),
                    (Status) campoStatus.getSelectedItem()
            );

            funcionarioController.editarFuncionario(idSelecionado, req);
            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Salário inválido. Use números (ex: 2500.00).",
                    "Erro", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove o funcionario selecionado depois de confirmar com o usuario
    private void excluirFuncionario() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um funcionário na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este funcionário?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                funcionarioController.excluirFuncionario(idSelecionado);
                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!");
                limparFormulario();
                carregarTabela();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Limpa todos os campos e remove a selecao da tabela
    private void limparFormulario() {
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoEndereco.setText("");
        campoSalario.setText("");
        campoCargo.setSelectedIndex(0);
        campoStatus.setSelectedIndex(0);
        campoSenha.setText("");
        idSelecionado = -1;
        tabela.clearSelection();
    }

    // Busca os funcionarios no service e atualiza a tabela
    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<FuncionarioResponse> lista = funcionarioController.listarFuncionarios();
        for (FuncionarioResponse f : lista) {
            modeloTabela.addRow(new Object[]{
                    f.id(),
                    f.nome(),
                    f.cpf(),
                    f.cargo(),
                    String.format("R$ %.2f", f.salario()),
                    f.status()
            });
        }
    }

    // Preenche os campos do formulario com os dados do funcionario selecionado
    private void preencherFormularioComSelecionado() {
        FuncionarioResponse f = funcionarioController.buscarFuncionarioPorId(idSelecionado);
        if (f == null) return;

        campoNome.setText(f.nome());
        campoCpf.setText(f.cpf());
        campoTelefone.setText(f.telefone());
        campoEmail.setText(f.email());
        campoEndereco.setText(f.endereco() != null ? f.endereco() : "");
        campoSalario.setText(String.valueOf(f.salario()));
        campoCargo.setSelectedItem(f.cargo());
        campoStatus.setSelectedItem(f.status());
        campoSenha.setText("");
    }

    // Valida os campos antes de salvar ou editar
    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.");
            return false;
        }

        String cpf = campoCpf.getText().trim().replaceAll("[./-]", "");
        if (!cpf.matches("\\d{11}|\\d{14}")) {
            JOptionPane.showMessageDialog(this, "CPF deve ter 11 dígitos ou CNPJ 14 dígitos.");
            return false;
        }
        if (!ValidacaoDocumentos.validar(cpf)) {
            JOptionPane.showMessageDialog(this, cpf.length() == 11 ? "CPF invalido. Verifique os digitos" : "CNPJ invalido. Verifique os digitos.");
            return false;
        }

        if (!campoTelefone.getText().trim().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Telefone deve ter 10 ou 11 dígitos.");
            return false;
        }

        if (campoEmail.getText().trim().isEmpty() ||
                !campoEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Email inválido.");
            return false;
        }

        if (campoSalario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Salário é obrigatório.");
            return false;
        }

        return true;
    }
}