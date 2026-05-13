package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import jakarta.persistence.EntityManager;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientePanel extends JPanel {

    private ClienteService clienteService;

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

    public ClientePanel(EntityManager em) {
        this.clienteService = new ClienteService(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        // ============ Lado Esquerdo - Formulario ============
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        // === Titulo ======================
        JLabel titulo = new JLabel("Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Atial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelEsquerdo.add(titulo, BorderLayout.NORTH);

        // === Campos ======================
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

        // ============ Lado Direito - Tabela ============
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        // === Colunas da tabela ======================
        String[] colunas = {"ID", "Nome", "Documento", "Telefone", "Email", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela nao editavel diretamente
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40); // Coluna ID pequena

        JScrollPane scroll = new JScrollPane(tabela);
        painelDireito.add(scroll, BorderLayout.CENTER);

        // ============ Divisao 50/50 ============
        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        divisor.setDividerLocation(420);
        divisor.setResizeWeight(0.45);

        add(divisor, BorderLayout.CENTER);

        // ============ Eventos ============
        botaoSalvar.addActionListener(e -> salvarCliente());
        botaoEditar.addActionListener(e -> editarCliente());
        botaoExcluir.addActionListener(e -> excluirCliente());
        botaoLimpar.addActionListener(e -> limparFormulario());

        // Ao clicar na tabela, preenche o formulario
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

            Cliente cliente = new Cliente(
                    campoNome.getText().trim(),
                    documento,
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    Status.ATIVO
            );

            clienteService.salvar(cliente);
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
            Cliente cliente = clienteService.buscarPorId(idSelecionado);
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente nao encontrado!");
                return;
            }

            String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");
            cliente.setNomeEmpresa(campoNome.getText().trim());
            cliente.setDocumento(documento);
            cliente.setEndereco(campoEndereco.getText().trim());
            cliente.setTelefone(campoTelefone.getText().trim());
            cliente.setEmail(campoEmail.getText().trim());
            cliente.setStatus((Status) campoStatus.getSelectedItem());

            clienteService.editar(cliente);
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
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este cliente?",
                "Confirmar exclusao", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                clienteService.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluido com sucesso!");
                limparFormulario();
                carregarTabela();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
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
        modeloTabela.setRowCount(0); // Limpa a tabela

        List<Cliente> clientes = clienteService.listar();
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
//        int linhaSelecionada = tabela.getSelectedRow();
//        if (linhaSelecionada != -1) return;
//
//        idSelecionado = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
//        Cliente cliente = clienteService.buscarPorId(idSelecionado);
//        if (cliente == null) return;

        Cliente cliente = clienteService.buscarPorId(idSelecionado);
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