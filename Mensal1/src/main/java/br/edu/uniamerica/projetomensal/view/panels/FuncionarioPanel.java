package br.edu.uniamerica.projetomensal.view.panels;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import jakarta.persistence.EntityManager;
import javax.swing.*;
import br.edu.uniamerica.projetomensal.view.Mascaras;
import java.util.*;
import java.time.*;
public class FuncionarioPanel extends PainelCadastro {
    public FuncionarioPanel() {
        super("Funcionários",new String[]{"ID","Nome","CPF","Cargo","Salário","Status"});
        campo("nome","Nome",new JTextField()); campo("cpf","CPF",Mascaras.campo(Mascaras.Tipo.CPF)); campo("telefone","Telefone",Mascaras.campo(Mascaras.Tipo.TELEFONE)); campo("email","E-mail",new JTextField());
        campo("endereco","Endereço",new JTextField()); campo("salario","Salário (R$)",new JTextField()); campo("cargo","Cargo",new JComboBox<>(Cargo.values()));
        campo("status","Status",new JComboBox<>(new Status[]{Status.ATIVO,Status.INATIVO})); campo("senha","Senha (vazia mantém a atual)",new JPasswordField()); filtrarStatus(Status.ATIVO,Status.INATIVO); iniciar();
    }
    protected Dados carregar(EntityManager em) {
        return new Dados(new FuncionarioController(em).listar().stream().map(f -> new Registro(f.getId(),new Object[]{f.getId(),f.getNome(),Mascaras.formatar(f.getCpf(),Mascaras.Tipo.CPF),f.getCargo(),dinheiro(f.getSalario()),f.getStatus()},
            valores("nome",f.getNome(),"cpf",f.getCpf(),"telefone",f.getTelefone(),"email",f.getEmail(),"endereco",f.getEndereco(),"salario",f.getSalario(),"cargo",f.getCargo(),"status",f.getStatus()))).toList(),Map.of());
    }
    protected void gravar(EntityManager em,int id,Map<String,Object> v) {
        var controller=new FuncionarioController(em);
        if(id==0) controller.salvar(texto(v,"nome"),texto(v,"cpf"),texto(v,"telefone"),texto(v,"email"),texto(v,"endereco"),numero(v,"salario"),(Cargo)v.get("cargo"),texto(v,"senha"));
        else controller.editar(id,texto(v,"nome"),texto(v,"cpf"),texto(v,"telefone"),texto(v,"email"),texto(v,"endereco"),numero(v,"salario"),(Cargo)v.get("cargo"),(Status)v.get("status"),texto(v,"senha"));
    }
    protected void remover(EntityManager em,int id) { new FuncionarioController(em).excluir(id); }
    protected String avisoExcluir() { return "Confirma inativar o funcionário? Seu histórico será preservado."; }
}
