package br.edu.uniamerica.projetomensal.view;
import br.edu.uniamerica.projetomensal.config.Sessao;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.view.panels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(Funcionario funcionario) {
        setIconImage(Icones.marcaJanela());
        setTitle("Detetizadora Master · Gestão de serviços"); setSize(1120,780); setMinimumSize(new Dimension(820,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null); setLayout(new BorderLayout());
        JPanel topo=new JPanel(new BorderLayout(16,16)); topo.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));
        JLabel marca=new JLabel("Detetizadora Master",Icones.criar(Icones.Tipo.MARCA,44),SwingConstants.LEFT); marca.setIconTextGap(14); marca.setFont(Tema.FONTE_MARCA); marca.setForeground(Tema.COR_TEXTO);
        JLabel usuario=new JLabel(funcionario.getNome()+" · "+funcionario.getCargo());
        JButton sair=new JButton("Sair da conta"); Tema.estilizarBotao(sair);
        JPanel conta=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0)); conta.add(usuario); conta.add(sair);
        topo.add(marca,BorderLayout.WEST); topo.add(conta,BorderLayout.EAST); add(topo,BorderLayout.NORTH);
        JTabbedPane abas=new JTabbedPane();
        abas.setUI(new NavegacaoUI());
        abas.addTab("Clientes",new ClientePanel());
        if(Sessao.gerente()) abas.addTab("Funcionários",new FuncionarioPanel());
        abas.addTab("Serviços",new ServicoPanel()); abas.addTab("Agenda",new AgendaPanel());
        if(Sessao.gerente()) abas.addTab("Relatórios",new RelatorioPanel());
        for(int i=0;i<abas.getTabCount();i++) abas.setIconAt(i,Icones.criar(Icones.secao(abas.getTitleAt(i)),20));
        add(abas,BorderLayout.CENTER);
        JLabel rodape=new JLabel("  Clientes · Serviços · Agendamentos | "+(Sessao.gerente()?"Acesso de gerente":"Acesso operacional"));
        rodape.setBorder(BorderFactory.createEmptyBorder(8,12,8,12)); rodape.setForeground(Tema.COR_TEXTO_SECUNDARIO); add(rodape,BorderLayout.SOUTH);
        sair.addActionListener(e->{ Sessao.encerrar(); dispose(); new LoginFrame().setVisible(true); });
        abas.addChangeListener(e->{ if(abas.getSelectedComponent() instanceof PainelCadastro painel) painel.atualizar(); });
        topo.setBackground(Tema.COR_FUNDO); conta.setBackground(Tema.COR_FUNDO); usuario.setForeground(Tema.COR_TEXTO);
    }
}
