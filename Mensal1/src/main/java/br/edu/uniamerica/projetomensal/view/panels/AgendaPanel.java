package br.edu.uniamerica.projetomensal.view.panels;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import jakarta.persistence.EntityManager;
import javax.swing.*;
import java.util.*;
import java.time.*;
public class AgendaPanel extends PainelCadastro {
    private final JTextField inicio=new JTextField(), fim=new JTextField();
    public AgendaPanel() {
        super("Agenda",new String[]{"ID","Data","Horário","Serviço","Cliente","Responsável","Status"});
        campo("servico","Serviço",new JComboBox<Servico>()); campo("funcionario","Responsável",new JComboBox<Funcionario>());
        campo("data","Data (DD/MM/AAAA)",new JTextField()); campo("hora","Horário (HH:MM)",new JTextField()); campo("observacoes","Observações",new JTextField());
        campo("status","Status",new JComboBox<>(new Status[]{Status.AGENDADO,Status.EM_ANDAMENTO,Status.CONCLUIDO,Status.INATIVO}));
        JPanel periodo=new JPanel(new java.awt.GridLayout(2,3,8,8)); periodo.add(new JLabel("De (DD/MM/AAAA)")); periodo.add(new JLabel("Até (DD/MM/AAAA)")); periodo.add(new JLabel(""));
        periodo.add(inicio); periodo.add(fim); JButton aplicar=new JButton("Filtrar datas"); periodo.add(aplicar);
        // O período filtra o carregamento, sem modificar os campos do cadastro.
        cabecalho.add(periodo,java.awt.BorderLayout.SOUTH); aplicar.addActionListener(e -> atualizar());
        br.edu.uniamerica.projetomensal.view.EstiloUtils.aplicarFundoEscuro(periodo); filtrarStatus(Status.AGENDADO,Status.EM_ANDAMENTO,Status.CONCLUIDO,Status.INATIVO); iniciar();
    }
    private volatile LocalDate de,ate;
    public void atualizar() {
        try {
            de=inicio.getText().isBlank()?null:LocalDate.parse(inicio.getText().trim(),DATA);
            ate=fim.getText().isBlank()?null:LocalDate.parse(fim.getText().trim(),DATA);
            if(de!=null&&ate!=null&&de.isAfter(ate)) throw new IllegalArgumentException();
        } catch(Exception ex) { informar("Período inválido. Use DD/MM/AAAA e início anterior ao fim.",true); return; }
        super.atualizar();
    }
    protected Dados carregar(EntityManager em) {
        var controller=new AgendaController(em);
        LocalDate inicio=de, fim=ate;
        var registros=controller.listar().stream().filter(a -> inicio==null||!a.getData().isBefore(inicio)).filter(a -> fim==null||!a.getData().isAfter(fim))
            .map(a -> new Registro(a.getId(),new Object[]{a.getId(),DATA.format(a.getData()),HORA.format(a.getHora()),a.getServico().getNomeServico(),a.getServico().getCliente().getNomeEmpresa(),a.getFuncionario()==null?"Sem responsável":a.getFuncionario().getNome(),a.getStatus()},
                valores("servico",a.getServico(),"funcionario",a.getFuncionario(),"data",DATA.format(a.getData()),"hora",HORA.format(a.getHora()),"observacoes",a.getObservacoes(),"status",a.getStatus()))).toList();
        return new Dados(registros,Map.of("servico",new ServicoController(em).listar(),"funcionario",controller.responsaveis()));
    }
    protected void gravar(EntityManager em,int id,Map<String,Object> v) {
        LocalDate data; LocalTime hora;
        try { data=LocalDate.parse(texto(v,"data"),DATA); }
        catch(Exception ex) { throw new br.edu.uniamerica.projetomensal.utils.ErroCampo("data", "Informe uma data válida: DD/MM/AAAA."); }
        try { hora=LocalTime.parse(texto(v,"hora"),HORA); }
        catch(Exception ex) { throw new br.edu.uniamerica.projetomensal.utils.ErroCampo("hora", "Informe um horário válido: HH:MM."); }
        Servico s=(Servico)v.get("servico"); Funcionario f=(Funcionario)v.get("funcionario");
        new AgendaController(em).salvar(id,s==null?0:s.getId(),f==null?0:f.getId(),data,hora,texto(v,"observacoes"),(Status)v.get("status"));
    }
    protected void remover(EntityManager em,int id) { new AgendaController(em).cancelar(id); }
    protected String avisoExcluir() { return "Confirma cancelar este agendamento? Ele ficará inativo no histórico."; }
}
