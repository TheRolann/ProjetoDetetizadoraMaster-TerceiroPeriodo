package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.*;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import br.edu.uniamerica.projetomensal.service.RelatorioService;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.time.*;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named="MASTER_INTEGRATION",matches="1")
class FluxosOperacaoTest {
    static EntityManager em;
    @BeforeAll static void iniciar() {
        String url=System.getenv("DB_URL");
        if(url==null||!url.matches("jdbc:postgresql://127\\.0\\.0\\.1:[0-9]+/master_test")) throw new IllegalStateException("Use somente master_test local.");
        FlywayConfig.migrar(); PersistenceManager.conectar(); em=PersistenceManager.getEntityManager();
    }
    @BeforeEach void gerente() { em.clear(); Sessao.iniciar(new FuncionarioController(em).autenticar("Maria Souza","1234")); }
    @AfterAll static void fechar() { Sessao.encerrar(); PersistenceManager.desconectar(); }

    @Test void operacoesPrivilegiadasSaoNegadasAoFuncionario() {
        Sessao.iniciar(new FuncionarioController(em).autenticar("João Silva","1234"));
        assertDoesNotThrow(()->new ClienteController(em).listar());
        assertDoesNotThrow(()->new ServicoController(em).listar());
        assertDoesNotThrow(()->new AgendaController(em).listar());
        assertThrows(NegocioException.class,()->new FuncionarioController(em).listar());
        assertThrows(NegocioException.class,()->new ClienteController(em).excluir(1));
        assertThrows(NegocioException.class,()->new ServicoController(em).excluir(1));
        assertThrows(NegocioException.class,()->new AgendaController(em).cancelar(1));
        assertThrows(NegocioException.class,()->new RelatorioController(em).gerar(RelatorioService.Tipo.FUNCIONARIOS));
        Cliente c=new ClienteController(em).listar().getFirst();
        assertThrows(NegocioException.class,()->new ClienteController(em).editar(c.getId(),c.getNomeEmpresa(),c.getDocumento(),c.getEndereco(),c.getTelefone(),c.getEmail(),Status.INATIVO));
    }
    @Test void semLoginNaoAcessaDados() {
        Sessao.encerrar();
        assertThrows(NegocioException.class,()->new ClienteController(em).listar());
        assertThrows(NegocioException.class,()->new AgendaController(em).listar());
    }
    @Test void gerenteNaoPerdeSeuProprioAcesso() {
        assertThrows(NegocioException.class,()->new FuncionarioController(em).excluir(Sessao.usuario().getId()));
        Funcionario f=Sessao.usuario();
        assertThrows(NegocioException.class,()->new FuncionarioController(em).editar(f.getId(),f.getNome(),f.getCpf(),f.getTelefone(),f.getEmail(),f.getEndereco(),f.getSalario(),Cargo.FUNCIONARIO,Status.ATIVO,""));
    }
    @Test void agendaBloqueiaConflitoEPermiteReagendarECancelar() {
        Servico s=servico(); AgendaController agenda=new AgendaController(em);
        LocalDate data=LocalDate.now().plusDays(20); LocalTime hora=LocalTime.of(8,0); int responsavel=1;
        String tag=UUID.randomUUID().toString();
        agenda.salvar(0,s.getId(),responsavel,data,hora,tag,Status.AGENDADO);
        Agenda a=agenda.listar().stream().filter(item->tag.equals(item.getObservacoes())).findFirst().orElseThrow();
        assertThrows(NegocioException.class,()->agenda.salvar(0,s.getId(),responsavel,data,hora,"Conflito",Status.AGENDADO));
        agenda.salvar(a.getId(),s.getId(),responsavel,data,hora.plusHours(1),tag,Status.AGENDADO);
        em.clear(); assertEquals(hora.plusHours(1),agenda.listar().stream().filter(item->item.getId()==a.getId()).findFirst().orElseThrow().getHora());
        agenda.cancelar(a.getId()); em.clear();
        assertEquals(Status.INATIVO,agenda.listar().stream().filter(item->item.getId()==a.getId()).findFirst().orElseThrow().getStatus());
    }
    @Test void agendaRejeitaPassadoEResponsavelAusente() {
        Servico s=servico(); AgendaController agenda=new AgendaController(em);
        assertThrows(NegocioException.class,()->agenda.salvar(0,s.getId(),1,LocalDate.now().minusDays(1),LocalTime.NOON,"Teste",Status.AGENDADO));
        assertThrows(NegocioException.class,()->agenda.salvar(0,s.getId(),0,LocalDate.now().plusDays(10),LocalTime.NOON,"Teste",Status.AGENDADO));
    }
    @Test void concluirServicoAtualizaAgendaEPreservaHistorico() {
        Servico s=servico(); AgendaController agenda=new AgendaController(em); String tag=UUID.randomUUID().toString();
        var antes=new RelatorioController(em).gerar(RelatorioService.Tipo.SERVICOS).resumo();
        agenda.salvar(0,s.getId(),1,LocalDate.now().plusDays(25),LocalTime.of(14,0),tag,Status.AGENDADO);
        new ServicoController(em).editar(s.getId(),s.getNomeServico(),s.getDescricao(),s.getData(),s.getValor(),s.getCliente(),Status.CONCLUIDO);
        em.clear();
        assertEquals(Status.CONCLUIDO,agenda.listar().stream().filter(a->tag.equals(a.getObservacoes())).findFirst().orElseThrow().getStatus());
        var depois=new RelatorioController(em).gerar(RelatorioService.Tipo.SERVICOS).resumo();
        assertEquals(valor(antes.get("Receita realizada"))+150,valor(depois.get("Receita realizada")),0.001);
        assertEquals(valor(antes.get("Receita prevista"))-150,valor(depois.get("Receita prevista")),0.001);
        assertThrows(NegocioException.class,()->new ServicoController(em).excluir(s.getId()));
        assertEquals(1L,((Number)em.createNativeQuery("SELECT COUNT(*) FROM historico_servicos WHERE servico_id = :id").setParameter("id",s.getId()).getSingleResult()).longValue());
    }
    @Test void inativarServicoCancelaAgendaPendente() {
        Servico s=servico(); AgendaController agenda=new AgendaController(em); String tag=UUID.randomUUID().toString();
        agenda.salvar(0,s.getId(),1,LocalDate.now().plusDays(26),LocalTime.of(15,0),tag,Status.AGENDADO);
        new ServicoController(em).editar(s.getId(),s.getNomeServico(),s.getDescricao(),s.getData(),s.getValor(),s.getCliente(),Status.INATIVO);
        em.clear(); assertEquals(Status.INATIVO,agenda.listar().stream().filter(a->tag.equals(a.getObservacoes())).findFirst().orElseThrow().getStatus());
    }
    @Test void funcionarioInativoSaiDaFolhaENaoFazLogin() {
        FuncionarioController funcionarios=new FuncionarioController(em); String nome="Teste "+UUID.randomUUID();
        String folhaAntes=new RelatorioController(em).gerar(RelatorioService.Tipo.FUNCIONARIOS).resumo().get("Folha ativa");
        funcionarios.salvar(nome,cpf(),"45999999999","teste@example.test","Rua teste",1000,Cargo.FUNCIONARIO,"senha-teste");
        Funcionario f=funcionarios.listar().stream().filter(item->nome.equals(item.getNome())).findFirst().orElseThrow();
        funcionarios.excluir(f.getId()); em.clear();
        assertNull(funcionarios.autenticar(nome,"senha-teste"));
        assertEquals(folhaAntes,new RelatorioController(em).gerar(RelatorioService.Tipo.FUNCIONARIOS).resumo().get("Folha ativa"));
    }
    @Test void novosContextosEnxergamClientesRecemCadastrados() {
        String nome="Cliente "+UUID.randomUUID(); ClienteController clientes=new ClienteController(em);
        clientes.salvar(nome,cpf(),"Rua teste","45999999999","teste@example.test",Status.ATIVO);
        boolean encontrado=PersistenceManager.executar(contexto->new ClienteController(contexto).listar().stream().anyMatch(c->nome.equals(c.getNomeEmpresa())));
        assertTrue(encontrado);
        Cliente c=clientes.listar().stream().filter(item->nome.equals(item.getNomeEmpresa())).findFirst().orElseThrow(); clientes.excluir(c.getId());
    }
    @Test void responsavelComAgendaPendentePrecisaReatribuirAntesDeInativar() {
        FuncionarioController funcionarios=new FuncionarioController(em); String nome="Agenda "+UUID.randomUUID();
        funcionarios.salvar(nome,cpf(),"45999999999","teste@example.test","Rua teste",1000,Cargo.FUNCIONARIO,"senha-teste");
        Funcionario f=funcionarios.listar().stream().filter(item->nome.equals(item.getNome())).findFirst().orElseThrow();
        Servico s=servico(); AgendaController agenda=new AgendaController(em); String tag=UUID.randomUUID().toString();
        agenda.salvar(0,s.getId(),f.getId(),LocalDate.now().plusDays(15),LocalTime.of(9,30),tag,Status.AGENDADO);
        assertThrows(NegocioException.class,()->funcionarios.excluir(f.getId()));
        Agenda a=agenda.listar().stream().filter(item->tag.equals(item.getObservacoes())).findFirst().orElseThrow();
        agenda.cancelar(a.getId()); funcionarios.excluir(f.getId());
    }
    private double valor(String moeda) { return Double.parseDouble(moeda.replaceAll("[^0-9,.-]", "").replace(".", "").replace(",", ".")); }
    @Test void contatoOpcionalPersistidoPermiteEdicaoComTelefoneFormatado() {
        ClienteController clientes=new ClienteController(em); String nome="Opcional "+UUID.randomUUID();
        clientes.salvar(nome,cpf(),"",null,"",Status.ATIVO);
        Cliente cliente=clientes.listar().stream().filter(c->nome.equals(c.getNomeEmpresa())).findFirst().orElseThrow();
        em.clear(); cliente=clientes.buscarPorId(cliente.getId());
        assertNull(cliente.getTelefone()); assertNull(cliente.getEmail()); assertNull(cliente.getEndereco());
        clientes.editar(cliente.getId(),nome,cliente.getDocumento(),"Rua teste","(45) 99999-9999","teste@example.test",Status.ATIVO);
        em.clear(); assertEquals("45999999999",clientes.buscarPorId(cliente.getId()).getTelefone());
        clientes.excluir(cliente.getId());
    }
    private Servico servico() {
        ServicoController controller=new ServicoController(em); String nome="Teste "+UUID.randomUUID();
        controller.salvar(nome,"Teste isolado",LocalDate.now().plusDays(20),150,new ClienteController(em).listar().getFirst(),Status.AGENDADO);
        return controller.listar().stream().filter(s->nome.equals(s.getNomeServico())).findFirst().orElseThrow();
    }
    private String cpf() {
        String base=String.format("%09d",new java.util.Random().nextInt(100000000,999999999));
        for(int tamanho=9;tamanho<=10;tamanho++) { int soma=0; for(int i=0;i<tamanho;i++) soma+=(base.charAt(i)-'0')*(tamanho+1-i); int digito=11-soma%11; base+=digito>=10?0:digito; }
        return base;
    }
}
