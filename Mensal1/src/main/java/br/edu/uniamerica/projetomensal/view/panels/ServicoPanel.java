package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.view.EstiloUtils;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ServicoPanel extends JPanel {

    private ServicoService servicoService;
    private ClienteService clienteService;

    // === Campos do formulario ======================
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JTextField campoData;
    private JTextField campoValor;
    private JComboBox<Cliente> campoCliente;
    private JComboBox<Status> campoStatus;

    // === Tabela ======================
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // === Botoes ======================
    private JButton botaoSalvar;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoLimpar;

    private int idSelecionado = -1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ServicoPanel(EntityManager em) {
        this.servicoService = new ServicoService(em);
        this.clienteService = new ClienteService(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {

        // ========== Lado Esquerdo - Formulario ==========
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JLabel titulo = new JLabel("Serviços", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelEsquerdo.add(titulo, BorderLayout.NORTH);

        JPanel painelCampos = new JPanel(new GridLayout(6, 2, 5, 8));

        painelCampos.add(new JLabel("Nome do Serviço:"));
        campoNome = new JTextField();
        painelCampos.add(campoNome);

        painelCampos.add(new JLabel("Descrição:"));
        campoDescricao = new JTextField();
        painelCampos.add(campoDescricao);

        painelCampos.add(new JLabel("Data (DD/MM/AAAA):"));
        campoData = new JTextField();
        painelCampos.add(campoData);

        painelCampos.add(new JLabel("Valor (R$):"));
        campoValor = new JTextField();
        painelCampos.add(campoValor);

        painelCampos.add(new JLabel("Cliente:"));
        campoCliente = new JComboBox<>();
        carregarClientes();
        painelCampos.add(campoCliente);

        painelCampos.add(new JLabel("Status:"));
        campoStatus = new JComboBox<>(new Status[]{
                Status.AGENDADO, Status.EM_ANDAMENTO, Status.CONCLUIDO, Status.INATIVO
        });
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
        painelDireito.setBorder(BorderFactory.createTitledBorder("Lista de Serviços"));

        String[] colunas = {"ID", "Nome", "Data", "Valor", "Cliente", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabela);
        painelDireito.add(scroll, BorderLayout.CENTER);

        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        divisor.setDividerLocation(420);
        divisor.setResizeWeight(0.45);
        add(divisor, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        // ========== Eventos ==========
        botaoSalvar.addActionListener(e  -> salvarServico());
        botaoEditar.addActionListener(e  -> editarServico());
        botaoExcluir.addActionListener(e -> excluirServico());
        botaoLimpar.addActionListener(e  -> limparFormulario());

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

    private void salvarServico() {
        if (!validarCampos()) return;

        try {
            LocalDate data = LocalDate.parse(campoData.getText().trim(), formatter);
            double valor = Double.parseDouble(campoValor.getText().trim().replace(",", "."));
            Cliente cliente = (Cliente) campoCliente.getSelectedItem();

            servicoService.cadastrar(
                    campoNome.getText().trim(),
                    campoDescricao.getText().trim(),
                    data,
                    valor,
                    cliente,
                    (Status) campoStatus.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Servico salvo com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarServico() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um servico na tabela para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            Servico servico = servicoService.buscarPorId(idSelecionado);
            if (servico == null) {
                JOptionPane.showMessageDialog(this, "Servico não encontrado.");
                return;
            }

            LocalDate data = LocalDate.parse(campoData.getText().trim(), formatter);
            double valor = Double.parseDouble(campoValor.getText().trim().replace(",", "."));

            servico.setNomeServico(campoNome.getText().trim());
            servico.setDescricao(campoDescricao.getText().trim());
            servico.setData(data);
            servico.setValor(valor);
            servico.setCliente((Cliente) campoCliente.getSelectedItem());
            servico.setStatus((Status) campoStatus.getSelectedItem());

            servicoService.editar(servico);
            JOptionPane.showMessageDialog(this, "Servico atualizado com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirServico() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um servico na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este serviço?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                servicoService.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Serviço excluído com sucesso!");
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
        campoDescricao.setText("");
        campoData.setText("");
        campoValor.setText("");
        campoStatus.setSelectedIndex(0);
        if (campoCliente.getItemCount() > 0) {
            campoCliente.setSelectedIndex(0);
        }
        idSelecionado = -1;
        tabela.clearSelection();
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);

        List<Servico> servicos = servicoService.listar();
        for (Servico s : servicos) {
            modeloTabela.addRow(new Object[]{
                    s.getId(),
                    s.getNomeServico(),
                    s.getData().format(formatter),
                    String.format("R$ %.2f", s.getValor()),
                    s.getCliente().getNomeEmpresa(),
                    s.getStatus()
            });
        }
    }

    private void carregarClientes() {
        campoCliente.removeAllItems();
        List<Cliente> clientes = clienteService.listar();
        for (Cliente c : clientes) {
            campoCliente.addItem(c);
        }
    }

    private void preencherFormularioComSelecionado() {
        Servico servico = servicoService.buscarPorId(idSelecionado);
        if (servico == null) return;

        campoNome.setText(servico.getNomeServico());
        campoDescricao.setText(servico.getDescricao());
        campoData.setText(servico.getData().format(formatter));
        campoValor.setText(String.valueOf(servico.getValor()));
        campoStatus.setSelectedItem(servico.getStatus());

        // Seleciona o cliente correto no JComboBox
        for (int i = 0; i < campoCliente.getItemCount(); i++) {
            Cliente c = campoCliente.getItemAt(i);
            if (c.getId() == servico.getCliente().getId()) {
                campoCliente.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome do servico e obrigatorio.");
            return false;
        }
        try {
            LocalDate.parse(campoData.getText().trim(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data invalida. Use o formato DD/MM/AAAA.");
            return false;
        }
        try {
            double valor = Double.parseDouble(campoValor.getText().trim().replace(",", "."));
            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "Valor deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor invalido. Use numeros (ex: 150.00).");
            return false;
        }
        if (campoCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.");
            return false;
        }
        return true;
    }
}