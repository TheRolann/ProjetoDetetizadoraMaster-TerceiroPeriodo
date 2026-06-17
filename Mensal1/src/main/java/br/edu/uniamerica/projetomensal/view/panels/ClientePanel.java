package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.controller.ClienteController;
import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.utils.ValidacaoDocumentos;
import br.edu.uniamerica.projetomensal.view.EstiloUtils;
import jakarta.persistence.EntityManager;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Painel responsavel por gerenciar a tela de clientes
// Contem o formulario para criar/editar clientes e uma tabela para listar
public class ClientePanel extends JPanel {

    // Controller que recebe as acoes da view e delega ao service
    private ClienteController clienteController;

    // === Campos do formulario ======================
    private JTextField campoNome;
    private JTextField campoDocumento;
    private JTextField campoEndereco;
    private JTextField campoTelefone;
    private JTextField campoEmail;
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

    // Construtor recebe um EntityManager e cria o controller usado pelo painel
    public ClientePanel(EntityManager em) {
        this.clienteController = new ClienteController(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        // ============ Lado Esquerdo - Formulario ============
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JLabel titulo = new JLabel("Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Atial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelEsquerdo.add(titulo, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 5, 8));

        painelCampos.add(new JLabel("Nome do Cliente:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Documento (CPF/CNPJ):"));
        campoDocumento = new JTextField();
        painelCampos.add(campoDocumento);

        painelCampos.add(new JLabel("Endereco"));
        campoEndereco = new JTextField();
        painelCampos.add(campoEndereco);

        painelCampos.add(new JLabel("Telefone:"));
        campoTelefone = new JTextField();
        painelCampos.add(campoTelefone);

        painelCampos.add(new JLabel("Email:"));
        campoEmail = new JTextField();
        painelCampos.add(campoEmail);

        painelCampos.add(new JLabel("Status:"));
        campoStatus = new JComboBox<>(new Status[]{Status.ATIVO, Status.INATIVO});
        painelCampos.add(campoStatus);

        painelEsquerdo.add(painelCampos, BorderLayout.CENTER);

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

        // ============ Lado Direito - Tabela ============
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        String[] colunas = {"ID", "Nome", "Documento", "Telefone", "Email", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);

        JScrollPane scroll = new JScrollPane(tabela);
        painelDireito.add(scroll, BorderLayout.CENTER);

        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        divisor.setDividerLocation(420);
        divisor.setResizeWeight(0.45);

        add(divisor, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        botaoSalvar.addActionListener(e -> salvarCliente());
        botaoEditar.addActionListener(e -> editarCliente());
        botaoExcluir.addActionListener(e -> excluirCliente());
        botaoLimpar.addActionListener(e -> limparFormulario());

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

    // ============ Metodos de Acao ============

    private void salvarCliente() {
        if (!validarCampos()) return;

        try {
            String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");

            clienteController.salvar(
                    campoNome.getText().trim(),
                    documento,
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    Status.ATIVO
            );

            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCliente() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");

            clienteController.editar(
                    idSelecionado,
                    campoNome.getText().trim(),
                    documento,
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    (Status) campoStatus.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCliente() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um cliente na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este cliente?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                clienteController.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluido com sucesso!");
                limparFormulario();
                carregarTabela();

            } catch (Exception ex) {
                String mensagemCompleta = getMensagemCompleta(ex);

                if (mensagemCompleta.contains("foreign key") ||
                        mensagemCompleta.contains("violates") ||
                        mensagemCompleta.contains("fkey")) {

                    JOptionPane.showMessageDialog(this,
                            "Nao e possivel excluir este cliente.\n" +
                                    "Ele possui servicos vinculados.\n" +
                                    "Exclua os servicos primeiro.",
                            "Erro ao excluir", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir: " + mensagemCompleta,
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private String getMensagemCompleta(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        Throwable atual = ex;
        while (atual != null) {
            if (atual.getMessage() != null) {
                sb.append(atual.getMessage()).append(" ");
            }
            atual = atual.getCause();
        }
        return sb.toString();
    }

    private void limparFormulario() {
        campoNome.setText("");
        campoDocumento.setText("");
        campoEndereco.setText("");
        campoTelefone.setText("");
        campoEmail.setText("");
        campoStatus.setSelectedIndex(0);
        idSelecionado = -1;
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Cliente> clientes = clienteController.listar();
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{
                    c.getId(),
                    c.getNomeEmpresa(),
                    c.getDocumento(),
                    c.getTelefone(),
                    c.getEmail(),
                    c.getStatus()
            });
        }
    }

    private void preencherFormularioComSelecionado() {
        Cliente cliente = clienteController.buscarPorId(idSelecionado);
        if (cliente == null) return;

        campoNome.setText(cliente.getNomeEmpresa());
        campoDocumento.setText(cliente.getDocumento());
        campoEndereco.setText(cliente.getEndereco());
        campoTelefone.setText(cliente.getTelefone());
        campoEmail.setText(cliente.getEmail());
        campoStatus.setSelectedItem(cliente.getStatus());
    }

    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e obrigatorio.");
            return false;
        }

        String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");
        if(!documento.matches("\\d{11}|\\d{14}")) {
            JOptionPane.showMessageDialog(this, "Documento deve ter 11 (CPF) ou 14 (CNPJ) digitos.");
            return false;
        }
        if (!ValidacaoDocumentos.validar(documento)) {
            JOptionPane.showMessageDialog(this, documento.length() == 11 ? "CPF invalido. Verifique os digitos" : "CNPJ invalido. Verifique os digitos.");
            return false;
        }

        if (campoEmail.getText().trim().isEmpty() || !campoEmail.getText().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(this, "Email invalido");
            return false;
        }

        if (!campoTelefone.getText().trim().matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(this, "Telefone deve ter 10 ou 11 digitos.");
            return false;
        }

        return true;
    }
}