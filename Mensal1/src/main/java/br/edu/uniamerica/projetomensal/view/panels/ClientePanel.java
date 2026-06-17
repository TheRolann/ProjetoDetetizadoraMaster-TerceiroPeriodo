package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.entity.ClienteEntity;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.model.service.ClienteService;
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

    // Service que executa as regras de negocio (salvar, editar, excluir, listar)
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

    // Construtor recebe um EntityManager e cria o service usado pelo painel
    // Tambem monta a interface e carrega os dados iniciais
    public ClientePanel(EntityManager em) {
        this.clienteService = new ClienteService(em);
        setLayout(new BorderLayout());
        inicializarComponentes();
        carregarTabela();
    }

    // Metodo que monta e configura todos os componentes visuais do painel
    // Divide a tela em formulario (esquerda) e tabela (direita)
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
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

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

    // Acao do botao Salvar: valida campos e solicita ao service salvar o cliente
    private void salvarCliente() {
        if (!validarCampos()) return;

        try {
            String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");

            ClienteEntity clienteEntity = new ClienteEntity(
                    campoNome.getText().trim(),
                    documento,
                    campoEndereco.getText().trim(),
                    campoTelefone.getText().trim(),
                    campoEmail.getText().trim(),
                    Status.ATIVO
            );

            clienteService.salvar(clienteEntity);
            JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Acao do botao Editar: atualiza o cliente selecionado
    private void editarCliente() {
        if (idSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validarCampos()) return;

        try {
            ClienteEntity clienteEntity = clienteService.buscarPorId(idSelecionado);
            if (clienteEntity == null) {
                JOptionPane.showMessageDialog(this, "Cliente nao encontrado!");
                return;
            }

            String documento = campoDocumento.getText().trim().replaceAll("[./-]", "");
            clienteEntity.setNomeEmpresa(campoNome.getText().trim());
            clienteEntity.setDocumento(documento);
            clienteEntity.setEndereco(campoEndereco.getText().trim());
            clienteEntity.setTelefone(campoTelefone.getText().trim());
            clienteEntity.setEmail(campoEmail.getText().trim());
            clienteEntity.setStatus((Status) campoStatus.getSelectedItem());

            clienteService.editar(clienteEntity);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Acao do botao Excluir: confirma e solicita exclusao via service
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
                clienteService.excluir(idSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente excluido com sucesso!");
                limparFormulario();
                carregarTabela();

            } catch (Exception ex) {
                // Busca a mensagem em toda a cadeia de causas
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

    // Percorre a cadeia de excecoes (causes) e concatena as mensagens
    // Util para extrair a causa real de erros de banco, por exemplo
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

    // Reseta os campos do formulario para o estado inicial
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

    // Busca os clientes no service e atualiza o conteudo da tabela
    private void carregarTabela() {
        modeloTabela.setRowCount(0); // Limpa a tabela

        List<ClienteEntity> clienteEntities = clienteService.listar();
        for (ClienteEntity c : clienteEntities) {
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

    // Carrega os dados do cliente selecionado no formulario para edicao
    private void preencherFormularioComSelecionado() {
//        int linhaSelecionada = tabela.getSelectedRow();
//        if (linhaSelecionada != -1) return;
//
//        idSelecionado = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
//        Cliente cliente = clienteService.buscarPorId(idSelecionado);
//        if (cliente == null) return;

        ClienteEntity clienteEntity = clienteService.buscarPorId(idSelecionado);
        if (clienteEntity == null) return;

        campoNome.setText(clienteEntity.getNomeEmpresa());
        campoDocumento.setText(clienteEntity.getDocumento());
        campoEndereco.setText(clienteEntity.getEndereco());
        campoTelefone.setText(clienteEntity.getTelefone());
        campoEmail.setText(clienteEntity.getEmail());
        campoStatus.setSelectedItem(clienteEntity.getStatus());
    }

    // Valida os campos do formulario antes de salvar ou editar
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