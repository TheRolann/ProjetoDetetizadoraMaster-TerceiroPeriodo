package br.edu.uniamerica.projetomensal.view.panels;

import br.edu.uniamerica.projetomensal.config.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.utils.ErroCampo;
import br.edu.uniamerica.projetomensal.view.*;
import jakarta.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.regex.Pattern;

// A lista e o formulário têm ciclos separados; persistência continua nos controllers.
public abstract class PainelCadastro extends JPanel {
    public record Registro(int id, Object[] colunas, Map<String,Object> campos) {}
    public record Dados(List<Registro> registros, Map<String,List<?>> opcoes) {}
    private record Entrada(String rotulo, JComponent componente, JTextArea erro) {}
    protected final Map<String,JComponent> campos = new LinkedHashMap<>();
    protected final JPanel formulario = new JPanel(new GridBagLayout());
    protected final JPanel cabecalho = new JPanel(new BorderLayout(16,16));
    protected final DefaultTableModel modelo;
    protected final JTable tabela;
    private final Map<String,Entrada> entradas = new LinkedHashMap<>();
    private final JTextField busca = new JTextField(22);
    private final JLabel mensagem = new JLabel("Carregando registros...");
    private final JComboBox<String> filtroStatus = new JComboBox<>();
    private final JButton novo = new JButton("Novo cadastro"), editar = new JButton("Editar selecionado"), excluir = new JButton("Excluir / inativar"), atualizar = new JButton("Atualizar");
    private final JButton salvar = new JButton("Salvar cadastro"), cancelar = new JButton("Cancelar");
    private final JTextArea mensagemForm = aviso();
    private final String titulo;
    private List<Registro> registros = List.of();
    private JDialog dialogo;
    private boolean ocupado;
    private int idEdicao;
    private int linhaFormulario;
    private String sucessoPendente;
    protected static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    protected static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final Set<String> CONTATO = Set.of("email","telefone","endereco");
    private static final Set<String> OPCIONAIS = Set.of("email","telefone","endereco","descricao","observacoes");

    protected PainelCadastro(String titulo, String[] colunas) {
        this.titulo = titulo;
        setLayout(new BorderLayout(20,20)); setBorder(BorderFactory.createEmptyBorder(24,24,20,24));
        JPanel identidade = new JPanel(new GridLayout(2,1,0,4));
        JLabel nome = new JLabel(titulo); nome.setFont(Tema.FONTE_TITULO);
        nome.setIcon(Icones.criar(Icones.secao(titulo),24)); nome.setIconTextGap(10);
        JLabel descricao = new JLabel("Consulte, cadastre e mantenha os registros atualizados.");
        descricao.setForeground(Tema.COR_TEXTO_SECUNDARIO); identidade.add(nome); identidade.add(descricao);
        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        for(JButton botao : List.of(atualizar,editar,excluir,novo)) acoes.add(botao);
        JPanel faixa=new JPanel(new BorderLayout(12,12)); faixa.add(identidade,BorderLayout.CENTER); faixa.add(acoes,BorderLayout.EAST);
        cabecalho.add(faixa,BorderLayout.NORTH); add(cabecalho,BorderLayout.NORTH);
        JPanel lista = new JPanel(new BorderLayout(12,12)); JPanel filtros = new JPanel(new BorderLayout(12,12));
        JLabel label = new JLabel("Buscar na tabela"); label.setLabelFor(busca); busca.getAccessibleContext().setAccessibleName("Buscar na tabela");
        label.setIcon(Icones.criar(Icones.Tipo.BUSCAR,16)); label.setIconTextGap(8);
        JPanel pesquisa = new JPanel(new BorderLayout(0,6)); pesquisa.add(label,BorderLayout.NORTH); pesquisa.add(busca,BorderLayout.CENTER);
        JPanel status = new JPanel(new BorderLayout(0,6)); status.add(new JLabel("Status"),BorderLayout.NORTH); status.add(filtroStatus,BorderLayout.CENTER);
        filtros.add(pesquisa,BorderLayout.CENTER); filtros.add(status,BorderLayout.EAST); lista.add(filtros,BorderLayout.NORTH);
        modelo = new DefaultTableModel(colunas,0) {
            public boolean isCellEditable(int linha,int coluna) { return false; }
            public Class<?> getColumnClass(int coluna) { return coluna == 0 ? Integer.class : Object.class; }
        };
        tabela = new JTable(modelo); tabela.setAutoCreateRowSorter(true); tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(64); lista.add(new JScrollPane(tabela),BorderLayout.CENTER);
        add(lista,BorderLayout.CENTER); add(mensagem,BorderLayout.SOUTH);
        novo.addActionListener(e -> abrir(false)); editar.addActionListener(e -> abrir(true)); atualizar.addActionListener(e -> atualizar()); excluir.addActionListener(e -> remover());
        salvar.addActionListener(e -> gravar()); cancelar.addActionListener(e -> { if(!ocupado) dialogo.dispose(); });
        tabela.getSelectionModel().addListSelectionListener(e -> estadoBotoes());
        tabela.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { if(e.getClickCount()==2&&!ocupado&&tabela.getSelectedRow()>=0) abrir(true); } });
        busca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); } public void removeUpdate(DocumentEvent e) { filtrar(); } public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
        filtroStatus.addActionListener(e -> filtrar());
        addComponentListener(new ComponentAdapter() { public void componentResized(ComponentEvent e) {
            boolean compacto = getWidth()<1050;
            faixa.remove(acoes); faixa.add(acoes,compacto?BorderLayout.SOUTH:BorderLayout.EAST); faixa.revalidate();
        }});
        EstiloUtils.aplicarFundoEscuro(this); nome.setFont(Tema.FONTE_TITULO); descricao.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        for(JButton b : List.of(atualizar,editar,excluir,cancelar)) Tema.estilizarBotaoSecundario(b);
        Tema.estilizarBotao(salvar); estadoBotoes();
    }

    protected void campo(String chave,String rotulo,JComponent componente) {
        campos.put(chave,componente); componente.setName(chave); componente.getAccessibleContext().setAccessibleName(rotulo);
        componente.setPreferredSize(new Dimension(260,Tema.ALTURA_CAMPO)); componente.setMinimumSize(new Dimension(80,Tema.ALTURA_CAMPO));
        JTextArea erro = aviso(); erro.setForeground(Tema.COR_ERRO); erro.setRows(2); erro.setVisible(false);
        entradas.put(chave,new Entrada(rotulo,componente,erro)); EstiloUtils.aplicarFundoEscuro(componente);
        if(componente instanceof JTextField texto) texto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { limparErro(chave); } public void removeUpdate(DocumentEvent e) { limparErro(chave); } public void changedUpdate(DocumentEvent e) { limparErro(chave); }
        });
        componente.addPropertyChangeListener("erroMascara",e -> { if(e.getNewValue()!=null) marcarErro(chave,e.getNewValue().toString()); });
    }
    protected void iniciar() { SwingUtilities.invokeLater(this::atualizar); }
    protected void filtrarStatus(Status... status) { filtroStatus.removeAllItems(); filtroStatus.addItem("Todos"); for(Status s:status) filtroStatus.addItem(s.name()); }
    protected abstract Dados carregar(EntityManager em);
    protected abstract void gravar(EntityManager em,int id,Map<String,Object> valores);
    protected abstract void remover(EntityManager em,int id);
    protected String avisoExcluir() { return "Confirma excluir este registro? Registros vinculados deverão ser inativados para preservar o histórico."; }

    public void atualizar() {
        if(ocupado) return;
        tarefa(this::carregar,dados -> {
            registros = dados.registros(); dados.opcoes().forEach((chave,valores) -> {
                JComboBox<Object> combo=(JComboBox<Object>)campos.get(chave); combo.removeAllItems(); valores.forEach(combo::addItem); combo.setSelectedIndex(-1);
            });
            modelo.setRowCount(0); registros.forEach(registro -> modelo.addRow(registro.colunas())); filtrar();
            informar((sucessoPendente==null?"":sucessoPendente+" · ")+registros.size()+" registros disponíveis.",false); sucessoPendente=null;
        });
    }
    private Registro selecionado() { int linha=tabela.getSelectedRow(); return linha<0?null:registros.get(tabela.convertRowIndexToModel(linha)); }
    private void abrir(boolean edicao) {
        if(ocupado) return; Registro registro=edicao?selecionado():null; if(edicao&&registro==null) return;
        idEdicao=registro==null?0:registro.id();
        campos.forEach((chave,campo) -> {
            limparErro(chave); Object valor=registro==null?null:registro.campos().get(chave);
            if(campo instanceof JPasswordField senha) senha.setText("");
            else if(campo instanceof JTextField texto) { texto.setText(""); texto.setText(valor==null?"":valor.toString()); }
            else if(campo instanceof JComboBox<?> combo) {
                combo.setSelectedIndex(registro==null&&combo.getItemCount()>0?0:-1);
                if(valor!=null) { combo.setSelectedItem(valor); for(int i=0;i<combo.getItemCount();i++) if(identificador(valor)>0&&identificador(valor)==identificador(combo.getItemAt(i))) { combo.setSelectedIndex(i); break; } }
            }
        });
        formulario.removeAll(); linhaFormulario=0; adicionarGrupo("Dados principais",false); if(campos.keySet().stream().anyMatch(CONTATO::contains)) adicionarGrupo("Contato",true);
        GridBagConstraints espaco=new GridBagConstraints(); espaco.gridx=0; espaco.gridy=linhaFormulario; espaco.gridwidth=2; espaco.weighty=1; formulario.add(Box.createVerticalGlue(),espaco);
        dialogo=new JDialog(SwingUtilities.getWindowAncestor(this),(edicao?"Editar · ":"Novo cadastro · ")+titulo,Dialog.ModalityType.DOCUMENT_MODAL);
        JPanel conteudo=new JPanel(new BorderLayout(16,16)); conteudo.setBorder(BorderFactory.createEmptyBorder(24,24,20,24)); conteudo.setBackground(Tema.COR_FUNDO);
        JPanel topo=new JPanel(new GridLayout(2,1,0,6)); topo.setBackground(Tema.COR_FUNDO);
        JLabel nome=new JLabel(edicao?"Editar registro":"Novo cadastro"); nome.setFont(Tema.FONTE_TITULO);
        nome.setIcon(Icones.criar(edicao?Icones.Tipo.EDITAR:Icones.Tipo.NOVO,24)); nome.setIconTextGap(10);
        JLabel legenda=new JLabel("* Campos obrigatórios. Os campos opcionais podem ficar vazios."); legenda.setForeground(Tema.COR_TEXTO_SECUNDARIO);
        topo.add(nome); topo.add(legenda); conteudo.add(topo,BorderLayout.NORTH);
        JScrollPane scroll=new JScrollPane(formulario); scroll.setBorder(BorderFactory.createEmptyBorder()); scroll.getViewport().setBackground(Tema.COR_FUNDO); scroll.getVerticalScrollBar().setUnitIncrement(18); conteudo.add(scroll,BorderLayout.CENTER);
        JPanel rodape=new JPanel(new BorderLayout(12,12)); rodape.setBackground(Tema.COR_FUNDO); mensagemForm.setText(""); rodape.add(mensagemForm,BorderLayout.CENTER);
        JPanel acoes=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); acoes.setBackground(Tema.COR_FUNDO); acoes.add(cancelar); acoes.add(salvar); rodape.add(acoes,BorderLayout.SOUTH); conteudo.add(rodape,BorderLayout.SOUTH);
        salvar.setText(edicao?"Salvar alterações":"Salvar cadastro"); dialogo.setContentPane(conteudo); dialogo.getRootPane().setDefaultButton(salvar);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.getRootPane().registerKeyboardAction(e -> { if(!ocupado) dialogo.dispose(); },KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW);
        Rectangle tela=GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); dialogo.setSize(Math.min(820,tela.width-80),Math.min(760,tela.height-80)); dialogo.setMinimumSize(new Dimension(620,480)); dialogo.setLocationRelativeTo(this);
        estadoBotoes(); dialogo.setVisible(true);
        campos.values().stream().filter(c -> c instanceof JPasswordField).forEach(c -> ((JPasswordField)c).setText(""));
    }
    private void adicionarGrupo(String tituloGrupo,boolean contato) {
        int linha=linhaFormulario;
        GridBagConstraints gbc=new GridBagConstraints(); gbc.gridx=0; gbc.gridy=linha; gbc.gridwidth=2; gbc.weightx=1; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.insets=new Insets(14,0,12,0);
        JLabel titulo=new JLabel(tituloGrupo); titulo.setFont(Tema.FONTE_BOLD); titulo.setForeground(Tema.COR_TEXTO_SECUNDARIO); formulario.add(titulo,gbc);
        titulo.setIcon(Icones.criar(contato?Icones.Tipo.CONTATO:Icones.secao(this.titulo),18)); titulo.setIconTextGap(8);
        var itens = contato ? List.of("email","telefone","endereco").stream().filter(entradas::containsKey).map(chave -> Map.entry(chave,entradas.get(chave))).toList() : entradas.entrySet().stream().filter(item -> !CONTATO.contains(item.getKey())).toList();
        int coluna=0; int proximaLinha=linha+1;
        for(var item:itens) {
            String chave=item.getKey(); Entrada entrada=item.getValue();
            boolean largo=Set.of("nome","endereco","descricao","observacoes").contains(chave);
            if(largo&&coluna==1) { proximaLinha++; coluna=0; }
            boolean opcional=OPCIONAIS.contains(chave)||(chave.equals("senha")&&idEdicao!=0);
            JPanel celula=new JPanel(new BorderLayout(0,6)); celula.setBackground(Tema.COR_FUNDO);
            JLabel rotulo=new JLabel((chave.equals("senha")?"Senha":entrada.rotulo())+(opcional?" (opcional)":" *")); rotulo.setLabelFor(entrada.componente());
            celula.add(rotulo,BorderLayout.NORTH); celula.add(entrada.componente(),BorderLayout.CENTER);
            JPanel notas=new JPanel(new BorderLayout()); notas.setBackground(Tema.COR_FUNDO); JLabel dica=new JLabel(dica(chave)); dica.setFont(Tema.FONTE_REGULAR.deriveFont(12f)); dica.setForeground(Tema.COR_TEXTO_SECUNDARIO); notas.add(dica,BorderLayout.NORTH); notas.add(entrada.erro(),BorderLayout.CENTER); celula.add(notas,BorderLayout.SOUTH);
            gbc=new GridBagConstraints(); gbc.gridx=coluna; gbc.gridy=proximaLinha; gbc.gridwidth=largo?2:1; gbc.weightx=largo?1:.5; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.anchor=GridBagConstraints.NORTH; gbc.insets=new Insets(0,coluna==1?12:0,14,!largo&&coluna==0?12:0); formulario.add(celula,gbc);
            if(largo||coluna==1) { proximaLinha++; coluna=0; } else coluna=1;
        }
        linhaFormulario=proximaLinha+(coluna==1?1:0);
    }

    private String dica(String chave) { return switch(chave) { case "documento" -> "CPF ou CNPJ: a pontuação é automática."; case "cpf" -> "11 dígitos; a pontuação é automática."; case "telefone" -> "Ex.: (45) 99999-9999"; case "email" -> "Ex.: nome@dominio.com"; case "endereco" -> "Rua, número e bairro, se disponíveis."; case "senha" -> idEdicao==0?"Defina uma senha de acesso.":"Deixe vazio para manter a senha atual."; default -> " "; }; }
    private void gravar() {
        if(ocupado) return; entradas.keySet().forEach(this::limparErro);
        for(var campo:campos.entrySet()) {
            Object erro=campo.getValue().getClientProperty("erroMascara");
            if(erro!=null) { marcarErro(campo.getKey(),erro.toString()); mensagemForm.setText("Corrija o campo indicado antes de salvar."); return; }
        }
        Map<String,Object> valores=new LinkedHashMap<>(); campos.forEach((chave,campo) -> valores.put(chave,valor(campo)));
        int id=idEdicao; tarefa(em -> { gravar(em,id,valores); return null; },resultado -> { dialogo.dispose(); sucessoPendente=id==0?"Cadastro salvo":"Alterações salvas"; atualizar(); });
    }
    private void remover() {
        Registro registro=selecionado(); if(registro==null||ocupado) return;
        if(JOptionPane.showConfirmDialog(this,avisoExcluir(),"Confirmar operação",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        tarefa(em -> { remover(em,registro.id()); return null; },resultado -> { sucessoPendente="Operação concluída"; atualizar(); });
    }
    private <T> void tarefa(Function<EntityManager,T> operacao,Consumer<T> sucesso) {
        ocupado=true; estadoBotoes(); informar("Processando...",false);
        new SwingWorker<T,Void>() {
            protected T doInBackground() { return PersistenceManager.executar(operacao); }
            protected void done() {
                ocupado=false; estadoBotoes();
                try { sucesso.accept(get()); } catch(Exception ex) {
                    String texto=Mensagens.erro(ex); informar(texto,true);
                    if(dialogo!=null&&dialogo.isVisible()) { mensagemForm.setText(texto); mensagemForm.setForeground(Tema.COR_ERRO); for(Throwable causa=ex;causa!=null;causa=causa.getCause()) if(causa instanceof ErroCampo campo) { marcarErro(campo.campo(),campo.getMessage()); break; } }
                }
            }
        }.execute();
    }
    private void marcarErro(String chave,String texto) { Entrada entrada=entradas.get(chave); if(entrada==null) return; entrada.erro().setText(texto); entrada.erro().setVisible(true); if(entrada.erro().getParent()!=null) entrada.erro().getParent().revalidate(); entrada.componente().putClientProperty("erroCampo",true); entrada.componente().setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Tema.COR_ERRO),BorderFactory.createEmptyBorder(7,10,7,10))); }
    private void limparErro(String chave) { Entrada entrada=entradas.get(chave); if(entrada==null) return; entrada.erro().setText(""); entrada.erro().setVisible(false); if(entrada.erro().getParent()!=null) entrada.erro().getParent().revalidate(); entrada.componente().putClientProperty("erroCampo",false); entrada.componente().setBorder(Tema.bordaCampo()); }
    private void estadoBotoes() {
        boolean selecionado=tabela.getSelectedRow()>=0; novo.setEnabled(!ocupado); atualizar.setEnabled(!ocupado); editar.setEnabled(!ocupado&&selecionado); excluir.setEnabled(!ocupado&&selecionado&&Sessao.gerente()); tabela.setEnabled(!ocupado);
        salvar.setEnabled(!ocupado); cancelar.setEnabled(!ocupado); campos.values().forEach(c -> c.setEnabled(!ocupado));
        if(dialogo!=null) dialogo.setDefaultCloseOperation(ocupado?JDialog.DO_NOTHING_ON_CLOSE:JDialog.DISPOSE_ON_CLOSE);
    }
    private void filtrar() {
        var sorter=(TableRowSorter<DefaultTableModel>)tabela.getRowSorter(); List<RowFilter<DefaultTableModel,Integer>> filtros=new ArrayList<>();
        if(!busca.getText().isBlank()) filtros.add(RowFilter.regexFilter("(?iu)"+Pattern.quote(busca.getText().trim())));
        if(filtroStatus.getSelectedItem()!=null&&!"Todos".equals(filtroStatus.getSelectedItem())) filtros.add(RowFilter.regexFilter("^"+Pattern.quote(filtroStatus.getSelectedItem().toString())+"$",modelo.getColumnCount()-1));
        sorter.setRowFilter(filtros.isEmpty()?null:RowFilter.andFilter(filtros)); estadoBotoes();
    }
    private int identificador(Object item) { if(item instanceof Cliente c) return c.getId(); if(item instanceof Servico s) return s.getId(); if(item instanceof Funcionario f) return f.getId(); return 0; }
    private Object valor(JComponent campo) { if(campo instanceof JPasswordField senha) return new String(senha.getPassword()); if(campo instanceof JTextField texto) return texto.getText().trim(); if(campo instanceof JComboBox<?> combo) return combo.getSelectedItem(); return null; }
    private static JTextArea aviso() { JTextArea area=new JTextArea(2,20); area.setEditable(false); area.setLineWrap(true); area.setWrapStyleWord(true); area.setBackground(Tema.COR_FUNDO); area.setForeground(Tema.COR_TEXTO_SECUNDARIO); area.setFont(Tema.FONTE_REGULAR.deriveFont(12f)); return area; }
    protected void informar(String texto,boolean erro) { mensagem.setText(texto); mensagem.setToolTipText(texto); mensagem.setForeground(erro?Tema.COR_ERRO:Tema.COR_TEXTO_SECUNDARIO); }
    protected static String texto(Map<String,Object> valores,String chave) { return Objects.toString(valores.get(chave),""); }
    protected static double numero(Map<String,Object> valores,String chave) { try { return Double.parseDouble(texto(valores,chave).replace(",",".")); } catch(NumberFormatException ex) { throw new ErroCampo(chave,"Informe um número válido, sem separador de milhar."); } }
    protected static Map<String,Object> valores(Object... pares) { Map<String,Object> map=new LinkedHashMap<>(); for(int i=0;i<pares.length;i+=2) map.put((String)pares[i],pares[i+1]); return map; }
    protected static String dinheiro(double valor) { return NumberFormat.getCurrencyInstance(Locale.of("pt","BR")).format(valor); }
}
