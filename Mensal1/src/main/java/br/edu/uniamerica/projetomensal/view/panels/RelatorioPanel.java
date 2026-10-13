package br.edu.uniamerica.projetomensal.view.panels;
import br.edu.uniamerica.projetomensal.config.*;
import br.edu.uniamerica.projetomensal.controller.RelatorioController;
import br.edu.uniamerica.projetomensal.service.RelatorioService;
import br.edu.uniamerica.projetomensal.utils.Csv;
import br.edu.uniamerica.projetomensal.view.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class RelatorioPanel extends JPanel {
    private final JComboBox<RelatorioService.Tipo> tipo=new JComboBox<>(RelatorioService.Tipo.values());
    private final JButton atualizar=new JButton("Atualizar relatório"),exportar=new JButton("Exportar CSV");
    private final JTable tabela=new JTable();
    private final JPanel resumo=new JPanel(new GridLayout(0,2,12,12));
    private final JLabel mensagem=new JLabel("Selecione um relatório. As receitas são calculadas pelo status dos serviços.");
    private RelatorioService.Dados dados;
    public RelatorioPanel() {
        setLayout(new BorderLayout(16,16)); setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        JPanel topo=new JPanel(new FlowLayout(FlowLayout.LEFT,12,0)); topo.add(new JLabel("Relatório")); topo.add(tipo); topo.add(atualizar); topo.add(exportar);
        add(topo,BorderLayout.NORTH); JPanel conteudo=new JPanel(new BorderLayout(12,12)); conteudo.add(resumo,BorderLayout.NORTH); conteudo.add(new JScrollPane(tabela),BorderLayout.CENTER); add(conteudo,BorderLayout.CENTER); add(mensagem,BorderLayout.SOUTH);
        exportar.setEnabled(false); tabela.setAutoCreateRowSorter(true); atualizar.addActionListener(e->carregar()); tipo.addActionListener(e->carregar()); exportar.addActionListener(e->exportar());
        EstiloUtils.aplicarFundoEscuro(this); carregar();
    }
    private void carregar() {
        RelatorioService.Tipo escolhido=(RelatorioService.Tipo)tipo.getSelectedItem();
        atualizar.setEnabled(false); tipo.setEnabled(false); exportar.setEnabled(false); mensagem.setText("Consultando dados...");
        new SwingWorker<RelatorioService.Dados,Void>() {
            protected RelatorioService.Dados doInBackground() { return PersistenceManager.executar(em->new RelatorioController(em).gerar(escolhido)); }
            protected void done() {
                atualizar.setEnabled(true); tipo.setEnabled(true);
                try {
                    dados=get(); tabela.setModel(new DefaultTableModel(dados.linhas().toArray(Object[][]::new),dados.colunas()) { public boolean isCellEditable(int r,int c){return false;} });
                    resumo.removeAll(); dados.resumo().forEach((nome,valor)->{
                        JPanel card=new JPanel(new BorderLayout(4,4)); card.setBackground(Tema.COR_FUNDO_CAMPO);
                        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,3,0,0,Tema.COR_DESTAQUE),BorderFactory.createEmptyBorder(10,12,10,12)));
                        JLabel titulo=new JLabel(nome); titulo.setForeground(Tema.COR_TEXTO_SECUNDARIO);
                        JTextArea numero=new JTextArea(valor,2,15); numero.setEditable(false); numero.setLineWrap(true); numero.setWrapStyleWord(true);
                        numero.setFont(Tema.FONTE_BOLD.deriveFont(15f)); numero.setBackground(Tema.COR_FUNDO_CAMPO); numero.setForeground(Tema.COR_TEXTO);
                        card.add(titulo,BorderLayout.NORTH); card.add(numero,BorderLayout.CENTER); resumo.add(card);
                    });
                    resumo.revalidate(); resumo.repaint(); EstiloUtils.estilizarTabela(tabela); exportar.setEnabled(true);
                    mensagem.setText(dados.linhas().size()+" registros · Receita realizada = serviços concluídos; prevista = serviços pendentes.");
                } catch(Exception ex) { mensagem.setText(Mensagens.erro(ex)); }
            }
        }.execute();
    }
    private void exportar() {
        Sessao.exigirGerente(); JFileChooser chooser=new JFileChooser(); chooser.setSelectedFile(new java.io.File("relatorio-"+tipo.getSelectedItem().toString().toLowerCase()+".csv"));
        if(chooser.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return;
        Path escolhido=chooser.getSelectedFile().toPath(); if(!escolhido.toString().toLowerCase().endsWith(".csv")) escolhido=Path.of(escolhido+".csv");
        final Path arquivo=escolhido;
        if(Files.exists(arquivo)&&JOptionPane.showConfirmDialog(this,"Substituir o arquivo existente?","Exportação",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        List<Object[]> linhas=new ArrayList<>();
        for(int r=0;r<tabela.getRowCount();r++) { Object[] linha=new Object[tabela.getColumnCount()]; for(int c=0;c<linha.length;c++) linha[c]=tabela.getValueAt(r,c); linhas.add(linha); }
        String[] colunas=dados.colunas(); exportar.setEnabled(false);
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception { Csv.salvar(arquivo,colunas,linhas); return null; }
            protected void done() { exportar.setEnabled(true); try { get(); mensagem.setText("CSV salvo em "+arquivo); } catch(Exception ex) { mensagem.setText("Não foi possível salvar o CSV. Verifique a pasta e tente novamente."); } }
        }.execute();
    }
}
