package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.view.EstiloUtils;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FuncionarioPanel extends JPanel {

    private FuncionarioService funcionarioService;

    // === Campos do formulario =====
    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JTextField campoEmail;
    private JTextField campoEndereco;
    private JTextField campoSalario;
    private JComboBox<Cargo> campoCargo;
    private JComboBox<Status> campoStatus;

    // === Tabela ======================
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // === Botoes ======================
    private JButton botaoSalvar;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;

    // Guarda o ID do cliente selecionado para editar/excluir
    private int idSelecionado = -1;

    public FuncionarioPanel(EntityManager em) {
        this.funcionarioService = new FuncionarioService(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

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
        JPanel painelCampos = new JPanel(new GridLayout(8, 2, 5, 8));

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

        painelEsquerdo.add(painelCampos, BorderLayout.CENTER);

        // === Botoes ======================
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoSalvar  = new JButton("Salvar");
        botaoEditar  = new JButton("Editar");
        botaoExcluir = new JButton("Excluir");
        botaoLimpar  = new JButton("Limpar");

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
        botaoSalvar.addActionListener(e  -> salvarFuncionario());
        botaoEditar.addActionListener(e  -> editarFuncionario());
        botaoExcluir.addActionListener(e -> excluirFuncionario());
        botaoLimpar.addActionListener(e  -> limparFormulario());

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

    private void salvarFuncionario() {
        if (!validarCampos()) return;

        try {
            String cpf = campoCpf.getText().trim().replaceAll("[./-]", "");
            double salario = Double.parseDouble(campoSalario.getText().trim().replace(",", "."));

            Funcionario funcionario = new Funcionario(
                    campoNome.getText().trim(),
                    cpf,
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    campoEndereco.getText().trim(),
                    salario,
                    (Cargo) campoCargo.getSelectedItem(),
                    Status.ATIVO
            );

            funcionarioService.salvar(funcionario);
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

    private void editarFuncionario() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um funcionario na tabela para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            Funcionario funcionario = funcionarioService.buscarPorId(idSelecionado);
            if (funcionario == null) {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado.");
                return;
            }

            String cpf = campoCpf.getText().trim().replaceAll("[./-]", "");
            double salario = Double.parseDouble(campoSalario.getText().trim().replace(",", "."));

            funcionario.setNome(campoNome.getText().trim());
            funcionario.setCpf(cpf);
            funcionario.setTelefone(campoTelefone.getText().trim());
            funcionario.setEmail(campoEmail.getText().trim());
            funcionario.setEndereco(campoEndereco.getText().trim());
            funcionario.setSalario(salario);
            funcionario.setCargo((Cargo) campoCargo.getSelectedItem());
            funcionario.setStatus((Status) campoStatus.getSelectedItem());

            funcionarioService.editar(funcionario);
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
                funcionarioService.excluir(idSelecionado);
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

    private void limparFormulario() {
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoEndereco.setText("");
        campoSalario.setText("");
        campoCargo.setSelectedIndex(0);
        campoStatus.setSelectedIndex(0);
        idSelecionado = -1;
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Funcionario> funcionarios = funcionarioService.listar();
        for (Funcionario f : funcionarios) {
            modeloTabela.addRow(new Object[]{
                    f.getId(),
                    f.getNome(),
                    f.getCpf(),
                    f.getCargo(),
                    String.format("R$ %.2f", f.getSalario()),
                    f.getStatus()
            });
        }
    }

    private void preencherFormularioComSelecionado() {
        Funcionario funcionario = funcionarioService.buscarPorId(idSelecionado);
        if (funcionario == null) return;

        campoNome.setText(funcionario.getNome());
        campoCpf.setText(funcionario.getCpf());
        campoTelefone.setText(funcionario.getTelefone());
        campoEmail.setText(funcionario.getEmail());
        campoEndereco.setText(funcionario.getEndereco() != null ? funcionario.getEndereco() : "");
        campoSalario.setText(String.valueOf(funcionario.getSalario()));
        campoCargo.setSelectedItem(funcionario.getCargo());
        campoStatus.setSelectedItem(funcionario.getStatus());
    }

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