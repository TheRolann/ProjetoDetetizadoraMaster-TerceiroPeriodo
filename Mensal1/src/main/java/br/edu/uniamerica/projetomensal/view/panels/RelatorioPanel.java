package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.view.EstiloUtils;
import br.edu.uniamerica.projetomensal.view.Tema;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioPanel extends JPanel {

    private ClienteService clienteService;
    private FuncionarioService funcionarioService;
    private ServicoService servicoService;

    private JPanel painelConteudo;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RelatorioPanel(EntityManager em) {
        this.clienteService    = new ClienteService(em);
        this.funcionarioService = new FuncionarioService(em);
        this.servicoService    = new ServicoService(em);

        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {

        // ========== LADO ESQUERDO - Botoes ==========
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));
        painelBotoes.setBorder(new EmptyBorder(20, 10, 20, 10));
        painelBotoes.setPreferredSize(new Dimension(130, 0));

        JButton botaoClientes     = new JButton("Clientes");
        JButton botaoFuncionarios = new JButton("Funcionários");
        JButton botaoServicos     = new JButton("Serviços");
        JButton botaoAgenda       = new JButton("Agenda");

        // Tamanho uniforme dos botoes
        Dimension tamanhoBotao = new Dimension(110, 35);
        botaoClientes.setMaximumSize(tamanhoBotao);
        botaoFuncionarios.setMaximumSize(tamanhoBotao);
        botaoServicos.setMaximumSize(tamanhoBotao);
        botaoAgenda.setMaximumSize(tamanhoBotao);

        botaoClientes.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoFuncionarios.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoServicos.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoAgenda.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelBotoes.add(Box.createVerticalStrut(10));
        painelBotoes.add(botaoClientes);
        painelBotoes.add(Box.createVerticalStrut(10));
        painelBotoes.add(botaoFuncionarios);
        painelBotoes.add(Box.createVerticalStrut(10));
        painelBotoes.add(botaoServicos);
        painelBotoes.add(Box.createVerticalStrut(10));
        painelBotoes.add(botaoAgenda);

        // ========== LADO DIREITO - Conteudo ==========
        painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setBorder(BorderFactory.createTitledBorder("Relatório"));

        // Mensagem inicial
        JLabel labelInicial = new JLabel("Selecione um relatório ao lado.", SwingConstants.CENTER);
        labelInicial.setFont(new Font("Arial", Font.ITALIC, 14));
        painelConteudo.add(labelInicial, BorderLayout.CENTER);

        // ========== DIVISAO ==========
        JSplitPane divisor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelBotoes, painelConteudo);
        divisor.setDividerLocation(130);
        divisor.setResizeWeight(0.0);
        add(divisor, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        // ========== EVENTOS ==========
        botaoClientes.addActionListener(e     -> mostrarRelatorioClientes());
        botaoFuncionarios.addActionListener(e -> mostrarRelatorioFuncionarios());
        botaoServicos.addActionListener(e     -> mostrarRelatorioServicos());
        botaoAgenda.addActionListener(e       -> mostrarRelatorioAgenda());
    }

    // ========== METODO AUXILIAR ==========
    // Limpa e atualiza o painel de conteudo
    private void atualizarConteudo(JComponent componente, String tituloBorda) {
        painelConteudo.removeAll();
        painelConteudo.setBackground(Tema.COR_FUNDO);  // adiciona isso
        painelConteudo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),
                tituloBorda,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                Tema.FONTE_BOLD,
                Tema.COR_DESTAQUE
        ));
        painelConteudo.add(componente, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(painelConteudo));
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    // ========== RELATORIO CLIENTES ==========
    private void mostrarRelatorioClientes() {
        List<Cliente> clientes = clienteService.listar();

        long ativos   = clientes.stream().filter(c -> c.getStatus() == Status.ATIVO).count();
        long inativos = clientes.stream().filter(c -> c.getStatus() == Status.INATIVO).count();

        // Painel principal
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Resumo no topo
        JPanel resumo = new JPanel(new GridLayout(3, 2, 5, 5));
        resumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
        resumo.add(new JLabel("Total de clientes:"));
        resumo.add(new JLabel(String.valueOf(clientes.size())));
        resumo.add(new JLabel("Ativos:"));
        resumo.add(new JLabel(String.valueOf(ativos)));
        resumo.add(new JLabel("Inativos:"));
        resumo.add(new JLabel(String.valueOf(inativos)));
        painel.add(resumo, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Documento", "Telefone", "Email", "Status"};
        Object[][] dados = new Object[clientes.size()][6];
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            dados[i] = new Object[]{
                    c.getId(), c.getNomeEmpresa(), c.getDocumento(),
                    c.getTelefone(), c.getEmail(), c.getStatus()
            };
        }
        JTable tabela = criarTabelaReadOnly(colunas, dados);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        atualizarConteudo(painel, "Relatório de Clientes");
    }

    // ========== RELATORIO FUNCIONARIOS ==========
    private void mostrarRelatorioFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.listar();

        long gerentes      = funcionarios.stream().filter(f -> f.getCargo() == Cargo.GERENTE).count();
        long qtdFuncionarios = funcionarios.stream().filter(f -> f.getCargo() == Cargo.FUNCIONARIO).count();
        double totalSalarios = funcionarios.stream().mapToDouble(Funcionario::getSalario).sum();

        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel resumo = new JPanel(new GridLayout(4, 2, 5, 5));
        resumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
        resumo.add(new JLabel("Total de funcionários:"));
        resumo.add(new JLabel(String.valueOf(funcionarios.size())));
        resumo.add(new JLabel("Gerentes:"));
        resumo.add(new JLabel(String.valueOf(gerentes)));
        resumo.add(new JLabel("Funcionários:"));
        resumo.add(new JLabel(String.valueOf(qtdFuncionarios)));
        resumo.add(new JLabel("Total salários a pagar:"));
        resumo.add(new JLabel(String.format("R$ %.2f", totalSalarios)));
        painel.add(resumo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "CPF", "Cargo", "Salário", "Status"};
        Object[][] dados = new Object[funcionarios.size()][6];
        for (int i = 0; i < funcionarios.size(); i++) {
            Funcionario f = funcionarios.get(i);
            dados[i] = new Object[]{
                    f.getId(), f.getNome(), f.getCpf(),
                    f.getCargo(), String.format("R$ %.2f", f.getSalario()), f.getStatus()
            };
        }
        JTable tabela = criarTabelaReadOnly(colunas, dados);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        atualizarConteudo(painel, "Relatório de Funcionários");
    }

    // ========== RELATORIO SERVICOS ==========
    private void mostrarRelatorioServicos() {
        List<Servico> servicos = servicoService.listar();

        double totalFaturado = servicos.stream()
                .filter(s -> s.getStatus() == Status.CONCLUIDO)
                .mapToDouble(Servico::getValor).sum();

        double totalPendente = servicos.stream()
                .filter(s -> s.getStatus() == Status.AGENDADO || s.getStatus() == Status.EM_ANDAMENTO)
                .mapToDouble(Servico::getValor).sum();

        Servico maisLucrativo = servicos.stream()
                .filter(s -> s.getStatus() == Status.CONCLUIDO)
                .max((a, b) -> Double.compare(a.getValor(), b.getValor()))
                .orElse(null);

        long concluidos   = servicos.stream().filter(s -> s.getStatus() == Status.CONCLUIDO).count();
        long emAndamento  = servicos.stream().filter(s -> s.getStatus() == Status.EM_ANDAMENTO).count();
        long agendados    = servicos.stream().filter(s -> s.getStatus() == Status.AGENDADO).count();

        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel resumo = new JPanel(new GridLayout(7, 2, 5, 5));
        resumo.setBorder(BorderFactory.createTitledBorder("Resumo"));
        resumo.add(new JLabel("Total faturado (concluídos):"));
        resumo.add(new JLabel(String.format("R$ %.2f", totalFaturado)));
        resumo.add(new JLabel("Total pendente previsto:"));
        resumo.add(new JLabel(String.format("R$ %.2f", totalPendente)));
        resumo.add(new JLabel("Serviço mais lucrativo:"));
        resumo.add(new JLabel(maisLucrativo != null ? maisLucrativo.getNomeServico() + " (R$ " + String.format("%.2f", maisLucrativo.getValor()) + ")" : "Nenhum"));
        resumo.add(new JLabel("Concluídos:"));
        resumo.add(new JLabel(String.valueOf(concluidos)));
        resumo.add(new JLabel("Em andamento:"));
        resumo.add(new JLabel(String.valueOf(emAndamento)));
        resumo.add(new JLabel("Agendados:"));
        resumo.add(new JLabel(String.valueOf(agendados)));
        resumo.add(new JLabel("Total de serviços:"));
        resumo.add(new JLabel(String.valueOf(servicos.size())));
        painel.add(resumo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome", "Data", "Valor", "Cliente", "Status"};
        Object[][] dados = new Object[servicos.size()][6];
        for (int i = 0; i < servicos.size(); i++) {
            Servico s = servicos.get(i);
            dados[i] = new Object[]{
                    s.getId(), s.getNomeServico(),
                    s.getData().format(formatter),
                    String.format("R$ %.2f", s.getValor()),
                    s.getCliente().getNomeEmpresa(),
                    s.getStatus()
            };
        }
        JTable tabela = criarTabelaReadOnly(colunas, dados);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        atualizarConteudo(painel, "Relatório de Serviços");
    }

    // ========== RELATORIO AGENDA ==========
    private void mostrarRelatorioAgenda() {
        List<Servico> servicos = servicoService.listar();

        LocalDate hoje  = LocalDate.now();
        LocalDate limite = hoje.plusMonths(3);

        // Filtra servicos dos proximos 3 meses que nao estao concluidos ou inativos
        List<Servico> agenda = servicos.stream()
                .filter(s -> s.getData() != null)
                .filter(s -> !s.getData().isBefore(hoje) && !s.getData().isAfter(limite))
                .filter(s -> s.getStatus() == Status.AGENDADO || s.getStatus() == Status.EM_ANDAMENTO)
                .sorted((a, b) -> a.getData().compareTo(b.getData()))
                .toList();

        double totalPrevisto = agenda.stream().mapToDouble(Servico::getValor).sum();

        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel resumo = new JPanel(new GridLayout(3, 2, 5, 5));
        resumo.setBorder(BorderFactory.createTitledBorder("Próximos 3 meses"));
        resumo.add(new JLabel("Período:"));
        resumo.add(new JLabel(hoje.format(formatter) + " até " + limite.format(formatter)));
        resumo.add(new JLabel("Serviços no período:"));
        resumo.add(new JLabel(String.valueOf(agenda.size())));
        resumo.add(new JLabel("Valor total previsto:"));
        resumo.add(new JLabel(String.format("R$ %.2f", totalPrevisto)));
        painel.add(resumo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome do Serviço", "Data", "Valor", "Cliente", "Status"};
        Object[][] dados = new Object[agenda.size()][6];
        for (int i = 0; i < agenda.size(); i++) {
            Servico s = agenda.get(i);
            dados[i] = new Object[]{
                    s.getId(), s.getNomeServico(),
                    s.getData().format(formatter),
                    String.format("R$ %.2f", s.getValor()),
                    s.getCliente().getNomeEmpresa(),
                    s.getStatus()
            };
        }
        JTable tabela = criarTabelaReadOnly(colunas, dados);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> EstiloUtils.aplicarFundoEscuro(this));

        atualizarConteudo(painel, "Agenda — Próximos 3 Meses");
    }

    // ========== AUXILIAR - Tabela nao editavel ==========
    private JTable criarTabelaReadOnly(String[] colunas, Object[][] dados) {
        JTable tabela = new JTable(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(40);
        return tabela;
    }
}