package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.config.Sessao;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import br.edu.uniamerica.projetomensal.repository.*;
import jakarta.persistence.EntityManager;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;

public class RelatorioService {
    public enum Tipo { CLIENTES, FUNCIONARIOS, SERVICOS, AGENDA }
    public record Dados(String[] colunas, List<Object[]> linhas, Map<String,String> resumo) {}
    private final EntityManager em;
    public RelatorioService(EntityManager em) { this.em=em; }
    public Dados gerar(Tipo tipo) {
        Sessao.exigirGerente();
        Map<String,String> resumo=new LinkedHashMap<>();
        return switch(tipo) {
            case CLIENTES -> {
                var lista=new ClienteRepository(em).listar();
                resumo.put("Clientes",String.valueOf(lista.size())); resumo.put("Ativos",String.valueOf(lista.stream().filter(c->c.getStatus()==Status.ATIVO).count()));
                resumo.put("Inativos",String.valueOf(lista.stream().filter(c->c.getStatus()==Status.INATIVO).count()));
                yield new Dados(new String[]{"ID","Nome","Documento","Telefone","E-mail","Status"},lista.stream().map(c->new Object[]{c.getId(),c.getNomeEmpresa(),c.getDocumento(),c.getTelefone(),c.getEmail(),c.getStatus()}).toList(),resumo);
            }
            case FUNCIONARIOS -> {
                var lista=new FuncionarioRepository(em).listar();
                resumo.put("Funcionários ativos",String.valueOf(lista.stream().filter(f->f.getStatus()==Status.ATIVO).count()));
                resumo.put("Cadastrados",String.valueOf(lista.size()));
                resumo.put("Cargos ativos","Gerentes: "+lista.stream().filter(f->f.getStatus()==Status.ATIVO&&f.getCargo()==Cargo.GERENTE).count()+" · Funcionários: "+lista.stream().filter(f->f.getStatus()==Status.ATIVO&&f.getCargo()==Cargo.FUNCIONARIO).count());
                resumo.put("Folha ativa",dinheiro(lista.stream().filter(f->f.getStatus()==Status.ATIVO).mapToDouble(Funcionario::getSalario).sum()));
                yield new Dados(new String[]{"ID","Nome","Cargo","Salário","Status"},lista.stream().map(f->new Object[]{f.getId(),f.getNome(),f.getCargo(),dinheiro(f.getSalario()),f.getStatus()}).toList(),resumo);
            }
            case SERVICOS -> {
                var lista=new ServicoRepository(em).listar();
                resumo.put("Receita realizada",dinheiro(lista.stream().filter(s->s.getStatus()==Status.CONCLUIDO).mapToDouble(Servico::getValor).sum()));
                resumo.put("Receita prevista",dinheiro(lista.stream().filter(s->s.getStatus()==Status.AGENDADO||s.getStatus()==Status.EM_ANDAMENTO).mapToDouble(Servico::getValor).sum()));
                resumo.put("Maior valor concluído",lista.stream().filter(s->s.getStatus()==Status.CONCLUIDO).max(Comparator.comparingDouble(Servico::getValor)).map(s->s.getNomeServico()+" · "+dinheiro(s.getValor())).orElse("Nenhum"));
                resumo.put("Serviços","Concluídos: "+lista.stream().filter(s->s.getStatus()==Status.CONCLUIDO).count()+" · Pendentes: "+lista.stream().filter(s->s.getStatus()==Status.AGENDADO||s.getStatus()==Status.EM_ANDAMENTO).count());
                yield new Dados(new String[]{"ID","Serviço","Data","Valor","Cliente","Status"},lista.stream().map(s->new Object[]{s.getId(),s.getNomeServico(),data(s.getData()),dinheiro(s.getValor()),s.getCliente().getNomeEmpresa(),s.getStatus()}).toList(),resumo);
            }
            case AGENDA -> {
                var lista=new AgendaRepository(em).listar().stream().filter(a->!a.getData().isBefore(LocalDate.now())&&!a.getData().isAfter(LocalDate.now().plusMonths(3))).filter(a->a.getStatus()==Status.AGENDADO||a.getStatus()==Status.EM_ANDAMENTO).toList();
                resumo.put("Período",data(LocalDate.now())+" a "+data(LocalDate.now().plusMonths(3))); resumo.put("Agendamentos pendentes",String.valueOf(lista.size()));
                yield new Dados(new String[]{"ID","Data","Hora","Serviço","Responsável","Status"},lista.stream().map(a->new Object[]{a.getId(),data(a.getData()),a.getHora(),a.getServico().getNomeServico(),a.getFuncionario()==null?"Sem responsável":a.getFuncionario().getNome(),a.getStatus()}).toList(),resumo);
            }
        };
    }
    private static String dinheiro(double valor) { return NumberFormat.getCurrencyInstance(Locale.of("pt","BR")).format(valor); }
    private static String data(LocalDate data) { return data.format(DateTimeFormatter.ofPattern("dd/MM/uuuu")); }
}
