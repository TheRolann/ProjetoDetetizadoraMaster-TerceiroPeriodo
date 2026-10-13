package br.edu.uniamerica.projetomensal.view.panels;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import jakarta.persistence.EntityManager;
import javax.swing.*;
import br.edu.uniamerica.projetomensal.view.Mascaras;
import java.util.*;
import java.time.*;
public class ClientePanel extends PainelCadastro {
    public ClientePanel() {
        super("Clientes", new String[]{"ID","Nome","Documento","Telefone","E-mail","Status"});
        campo("nome","Nome do cliente",new JTextField()); campo("documento","CPF / CNPJ",Mascaras.campo(Mascaras.Tipo.DOCUMENTO));
        campo("endereco","Endereço",new JTextField()); campo("telefone","Telefone",Mascaras.campo(Mascaras.Tipo.TELEFONE)); campo("email","E-mail",new JTextField());
        campo("status","Status",new JComboBox<>(new Status[]{Status.ATIVO,Status.INATIVO})); filtrarStatus(Status.ATIVO,Status.INATIVO); iniciar();
    }
    protected Dados carregar(EntityManager em) {
        return new Dados(new ClienteController(em).listar().stream().map(c -> new Registro(c.getId(),new Object[]{c.getId(),c.getNomeEmpresa(),Mascaras.formatar(c.getDocumento(),Mascaras.Tipo.DOCUMENTO),Mascaras.formatar(c.getTelefone(),Mascaras.Tipo.TELEFONE),c.getEmail(),c.getStatus()},
            valores("nome",c.getNomeEmpresa(),"documento",c.getDocumento(),"endereco",c.getEndereco(),"telefone",c.getTelefone(),"email",c.getEmail(),"status",c.getStatus()))).toList(),Map.of());
    }
    protected void gravar(EntityManager em,int id,Map<String,Object> v) {
        var controller=new ClienteController(em);
        if(id==0) controller.salvar(texto(v,"nome"),texto(v,"documento"),texto(v,"endereco"),texto(v,"telefone"),texto(v,"email"),(Status)v.get("status"));
        else controller.editar(id,texto(v,"nome"),texto(v,"documento"),texto(v,"endereco"),texto(v,"telefone"),texto(v,"email"),(Status)v.get("status"));
    }
    protected void remover(EntityManager em,int id) { new ClienteController(em).excluir(id); }
}
