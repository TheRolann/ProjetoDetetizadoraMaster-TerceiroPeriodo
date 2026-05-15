package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewFuncionario {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("detetizadora_master");
        EntityManager em = emf.createEntityManager();
        FuncionarioService funcionarioService = new FuncionarioService(em);

        JFrame frame = new JFrame();
        frame.setSize(1200,700);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());


        JPanel containerForm = new JPanel(new BorderLayout());
        containerForm.setPreferredSize(new Dimension(650,700));

        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        painelForm.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Cadastro de Funcionários"),
                        BorderFactory.createEmptyBorder(15,15,15,15)
                )
        );
        frame.getContentPane().setBackground(new Color(245,245,245));
        painelForm.setBackground(Color.WHITE);
        containerForm.add(painelForm, BorderLayout.NORTH);

        frame.add(containerForm, BorderLayout.WEST);

        //Primeira Linha do Formulario
        JPanel linha1 = criarLinha();
        painelForm.add(linha1);
        painelForm.add(espacamentoVertical(8));
        painelForm.setPreferredSize(new Dimension(650,600));

        JLabel id = criarLabel("Id");
        linha1.add(id);

        JTextField txtId = criarCampo(80,80);
        txtId.setEditable(false);
        linha1.add(txtId);
        linha1.add(espacamentoHorizontal(20));

        JLabel nome = criarLabel("Nome");
        linha1.add(nome);
        linha1.add(espacamentoHorizontal(5));

        JTextField txtNome = criarCampo(250,300);
        linha1.add(txtNome);
        linha1.add(espacamentoHorizontal(20));

        //Segunda linha do Formulario
        JPanel linha2 = criarLinha();
        painelForm.add(linha2);
        painelForm.add(espacamentoVertical(25));

        JLabel cpf = criarLabel("CPF");
        linha2.add(cpf);
        linha2.add(espacamentoHorizontal(5));

        JTextField txtCpf = criarCampo(160,180);
        linha2.add(txtCpf);
        linha2.add(espacamentoHorizontal(20));

        JLabel telefone = criarLabel("Telefone");
        linha2.add(telefone);
        linha2.add(espacamentoHorizontal(5));

        JTextField txtTelefone = criarCampo(160,180);
        linha2.add(txtTelefone);
        linha2.add(espacamentoHorizontal(20));

        //Terceira linha do Formulario
        JPanel linha3 = criarLinha();
        painelForm.add(linha3);
        painelForm.add(espacamentoVertical(25));

        JLabel email = criarLabel("Email");
        linha3.add(email);
        linha3.add(espacamentoHorizontal(5));

        JTextField txtEmail = criarCampo(300,350);
        linha3.add(txtEmail);
        linha3.add(espacamentoHorizontal(20));

        //Quarta linha do Formulario
        JPanel linha4 = criarLinha();
        painelForm.add(linha4);
        painelForm.add(espacamentoVertical(25));

        JLabel cargo = criarLabel("Cargo");
        linha4.add(cargo);
        linha4.add(espacamentoHorizontal(5));

        JComboBox<Cargo> txtCargo = criarComboBox(Cargo.values(),180,220);
        linha4.add(txtCargo);
        linha4.add(espacamentoHorizontal(20));

        JLabel salario = criarLabel("Salario");
        linha4.add(salario);
        linha4.add(espacamentoHorizontal(5));

        JTextField txtSalario = criarCampo(120,140);
        linha4.add(txtSalario);
        linha4.add(espacamentoHorizontal(20));

        JLabel status = criarLabel("Status");
        linha4.add(status);
        linha4.add(espacamentoHorizontal(5));

        JComboBox<Status> txtStatus = criarComboBox(new Status[]{Status.ATIVO, Status.INATIVO},120,140);
        linha4.add(txtStatus);
        linha4.add(espacamentoHorizontal(20));

        //Quinta linha do Formulario
        JPanel linha5 = criarLinha();
        painelForm.add(linha5);
        painelForm.add(espacamentoVertical(25));

        JLabel endereco = criarLabel("Endereço");
        linha5.add(endereco);
        linha5.add(espacamentoHorizontal(5));

        JTextField txtEndereco = criarCampo(350,450);
        linha5.add(txtEndereco);
        linha5.add(espacamentoHorizontal(20));

        //Tabela
        JTable tblFuncionario = new JTable();
        DefaultTableModel tblModelo = new DefaultTableModel(new Object[]{"Id", "Nome", "CPF", "Cargo", "Status"},0);
        tblFuncionario.setModel(tblModelo);

        //Sexta linha do Formulario
        JPanel linha6 = new JPanel();
        linha6.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        painelForm.add(linha6);
        painelForm.add(espacamentoVertical(25));
        linha6.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton novo = criarBotao("Novo Funcionario");
        linha6.add(novo);
        linha6.add(espacamentoHorizontal(10));

        JButton editar = criarBotao("Editar");
        linha6.add(editar);
        linha6.add(espacamentoHorizontal(10));

        JButton excluir = criarBotao("Excluir");
        linha6.add(excluir);
        linha6.add(espacamentoHorizontal(10));

        JButton limpar = criarBotao("Limpar");
        linha6.add(limpar);
        linha6.add(espacamentoHorizontal(10));

        novo.addActionListener(e -> {
            try {
                Funcionario funcionario = new Funcionario();

                funcionario.setNome(txtNome.getText());
                funcionario.setCpf(txtCpf.getText());
                funcionario.setTelefone(txtTelefone.getText());
                funcionario.setEmail(txtEmail.getText());
                funcionario.setEndereco(txtEndereco.getText());
                funcionario.setCargo((Cargo) txtCargo.getSelectedItem());
                funcionario.setStatus((Status) txtStatus.getSelectedItem());
                funcionario.setSalario(Double.parseDouble(txtSalario.getText()));

                funcionarioService.salvar(funcionario);
                carregarTabela(tblModelo, funcionarioService);

                JOptionPane.showMessageDialog(null, "Funcionário salvo com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        editar.addActionListener(e -> {

            try {
                Funcionario funcionario = new Funcionario();

                funcionario.setId(Integer.parseInt(txtId.getText()));
                funcionario.setNome(txtNome.getText());
                funcionario.setCpf(txtCpf.getText());
                funcionario.setTelefone(txtTelefone.getText());
                funcionario.setEmail(txtEmail.getText());
                funcionario.setEndereco(txtEndereco.getText());
                funcionario.setCargo((Cargo) txtCargo.getSelectedItem());
                funcionario.setStatus((Status) txtStatus.getSelectedItem());
                funcionario.setSalario(Double.parseDouble(txtSalario.getText()));

                funcionarioService.editar(funcionario);
                JOptionPane.showMessageDialog(null, "Funcionário editado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        excluir.addActionListener(e -> {
            try {
                int idExcluir = Integer.parseInt(txtId.getText());
                funcionarioService.excluir(idExcluir);
                JOptionPane.showMessageDialog(null, "Funcionário excluído com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        limpar.addActionListener(e -> {
            limparCampos( txtId, txtNome, txtCpf, txtTelefone, txtEmail,
                    txtEndereco, txtSalario, txtCargo, txtStatus
            );
        });

        //Painel da Direita

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(
                BorderFactory.createTitledBorder("Funcionários")
        );

        frame.add(painelLista, BorderLayout.CENTER);

        painelLista.setPreferredSize(new Dimension(280,1));
        //Filtros
        JPanel painelFiltro = new JPanel();
        painelLista.add(painelFiltro, BorderLayout.NORTH);
        JLabel pesquisa = new JLabel("Pesquisa");
        JTextField txtPesquisa = new JTextField();
        txtPesquisa.setPreferredSize(new Dimension(220,30));
        painelFiltro.add(pesquisa);
        painelFiltro.add(txtPesquisa);
        painelFiltro.add(pesquisa);

        tblFuncionario.setRowHeight(28);
        tblFuncionario.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tblFuncionario);
        painelLista.add(scroll, BorderLayout.CENTER);

        carregarTabela(tblModelo, funcionarioService);

        painelForm.add(Box.createVerticalGlue());
        frame.setVisible(true);

    }

    private static JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);

        botao.setPreferredSize(new Dimension(140, 35));
        botao.setMaximumSize(new Dimension(140, 35));
        botao.setFocusPainted(false);
        botao.setBackground(new Color(52, 73, 94));
        botao.setForeground(Color.WHITE);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }

    private static JTextField criarCampo(int larguraPreferida, int larguraMaxima) {
        JTextField campo = new JTextField();

        campo.setPreferredSize(new Dimension(larguraPreferida, 38));
        campo.setMaximumSize(new Dimension(larguraMaxima, 38));
        return campo;
    }

    private static <T> JComboBox<T> criarComboBox(
            T[] itens,
            int larguraPreferida,
            int larguraMaxima
    ) {
        JComboBox<T> comboBox = new JComboBox<>(itens);
        comboBox.setPreferredSize(new Dimension(larguraPreferida,32));
        comboBox.setMaximumSize(new Dimension(larguraMaxima,32));
        comboBox.setFocusable(false);
        return comboBox;
    }

    private static JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setPreferredSize(new Dimension(90,25));
        return label;
    }

    private static JPanel criarLinha() {
        JPanel linha = new JPanel();
        linha.setLayout(new BoxLayout(linha, BoxLayout.X_AXIS));
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
        linha.setBackground(Color.WHITE);
        return linha;
    }

    private static Component espacamentoHorizontal(int tamanho) {
        return Box.createHorizontalStrut(tamanho);
    }

    private static Component espacamentoVertical(int tamanho) {
        return Box.createVerticalStrut(tamanho);
    }

    private static void limparCampos(JTextField txtId, JTextField txtNome, JTextField txtCpf,
                                     JTextField txtTelefone, JTextField txtEmail, JTextField txtEndereco,
                                     JTextField txtSalario, JComboBox<Cargo> txtCargo, JComboBox<Status> txtStatus) {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtSalario.setText("");
        txtCargo.setSelectedIndex(0);
        txtStatus.setSelectedIndex(0);
    }

    private static void carregarTabela(DefaultTableModel modelo, FuncionarioService service) {
        modelo.setRowCount(0);
        for (Funcionario funcionario : service.listar()){
            modelo.addRow(new Object[]{
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getCpf(),
                    funcionario.getCargo(),
                    funcionario.getStatus()
            });
        }
    }
}

