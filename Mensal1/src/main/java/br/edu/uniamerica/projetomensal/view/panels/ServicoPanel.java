package br.edu.uniamerica.projetomensal.view.panels;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import jakarta.persistence.EntityManager;
import javax.swing.*;
import java.util.*;
import java.time.*;
public class ServicoPanel extends PainelCadastro {
    public ServicoPanel() {
        super("Serviços",new String[]{"ID","Nome","Data","Valor","Cliente","Status"});
        campo("nome","Nome do serviço",new JTextField()); campo("descricao","Descrição",new JTextField()); campo("data","Data (DD/MM/AAAA)",new JTextField()); campo("valor","Valor (R$)",new JTextField());
        campo("cliente","Cliente",new JComboBox<Cliente>()); campo("status","Status",new JComboBox<>(new Status[]{Status.AGENDADO,Status.EM_ANDAMENTO,Status.CONCLUIDO,Status.INATIVO})); filtrarStatus(Status.AGENDADO,Status.EM_ANDAMENTO,Status.CONCLUIDO,Status.INATIVO); iniciar();
    }
    protected Dados carregar(EntityManager em) {
        var registros=new ServicoController(em).listar().stream().map(s -> new Registro(s.getId(),new Object[]{s.getId(),s.getNomeServico(),DATA.format(s.getData()),dinheiro(s.getValor()),s.getCliente().getNomeEmpresa(),s.getStatus()},
            valores("nome",s.getNomeServico(),"descricao",s.getDescricao(),"data",DATA.format(s.getData()),"valor",s.getValor(),"cliente",s.getCliente(),"status",s.getStatus()))).toList();
        return new Dados(registros,Map.of("cliente",new ClienteController(em).listar()));
    }
    protected void gravar(EntityManager em,int id,Map<String,Object> v) {
        LocalDate data;
        try { data=LocalDate.parse(texto(v,"data"),DATA); } catch(Exception ex) { throw new br.edu.uniamerica.projetomensal.utils.ErroCampo("data", "Informe uma data válida no formato DD/MM/AAAA."); }
        var controller=new ServicoController(em);
        if(id==0) controller.salvar(texto(v,"nome"),texto(v,"descricao"),data,numero(v,"valor"),(Cliente)v.get("cliente"),(Status)v.get("status"));
        else controller.editar(id,texto(v,"nome"),texto(v,"descricao"),data,numero(v,"valor"),(Cliente)v.get("cliente"),(Status)v.get("status"));
    }
    protected void remover(EntityManager em,int id) { new ServicoController(em).excluir(id); }
}
