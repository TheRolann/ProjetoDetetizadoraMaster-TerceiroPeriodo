package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.*;
import br.edu.uniamerica.projetomensal.controller.*;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

// Só roda mediante opt-in e em um banco explicitamente destinado aos testes.
@EnabledIfEnvironmentVariable(named = "MASTER_INTEGRATION", matches = "1")
class BancoIntegracaoTest {
    private static EntityManager em;

    @BeforeAll
    static void iniciar() {
        String url = System.getenv("DB_URL");
        if (url == null || !url.matches("jdbc:postgresql://127\\.0\\.0\\.1:[0-9]+/master_test")) {
            throw new IllegalStateException("Use um banco local chamado master_test exclusivo para testes.");
        }
        FlywayConfig.migrar();
        // Segunda execução deve apenas validar, sem repetir dados iniciais.
        FlywayConfig.migrar();
        PersistenceManager.conectar();
        em = PersistenceManager.getEntityManager();
    }

    @AfterAll
    static void fechar() { PersistenceManager.desconectar(); }

    @BeforeEach
    void limparCache() { em.clear(); Sessao.iniciar(new FuncionarioController(em).autenticar("Maria Souza", "1234")); }

    @Test
    void dadosIniciaisPermitemLoginComBcrypt() {
        FuncionarioController controller = new FuncionarioController(em);
        Funcionario funcionario = controller.autenticar("João Silva", "1234");
        assertNotNull(funcionario);
        assertTrue(funcionario.getSenha().startsWith("$2"));
        assertNull(controller.autenticar("João Silva", "senha-errada"));
    }

    @Test
    void edicaoInvalidaNaoMudaClienteNoBanco() {
        ClienteController controller = new ClienteController(em);
        Cliente cliente = controller.listar().getFirst();
        String nome = cliente.getNomeEmpresa();
        assertThrows(NegocioException.class, () -> controller.editar(cliente.getId(), "Nome rejeitado", "123",
                cliente.getEndereco(), cliente.getTelefone(), cliente.getEmail(), cliente.getStatus()));
        em.clear();
        assertEquals(nome, controller.buscarPorId(cliente.getId()).getNomeEmpresa());
    }

    @Test
    void documentoDuplicadoNaoAlteraOutroCliente() {
        ClienteController controller = new ClienteController(em);
        var clientes = controller.listar();
        Cliente primeiro = clientes.get(0), segundo = clientes.get(1);
        String documento = segundo.getDocumento();
        assertThrows(NegocioException.class, () -> controller.editar(segundo.getId(), segundo.getNomeEmpresa(),
                primeiro.getDocumento(), segundo.getEndereco(), segundo.getTelefone(), segundo.getEmail(), segundo.getStatus()));
        em.clear();
        assertEquals(documento, controller.buscarPorId(segundo.getId()).getDocumento());
        assertDoesNotThrow(() -> controller.editar(segundo.getId(), segundo.getNomeEmpresa(), documento,
                segundo.getEndereco(), segundo.getTelefone(), segundo.getEmail(), segundo.getStatus()));
    }

    @Test
    void cpfDuplicadoNaoAlteraNomeNemSenha() {
        FuncionarioController controller = new FuncionarioController(em);
        var funcionarios = controller.listar();
        Funcionario primeiro = funcionarios.get(0), segundo = funcionarios.get(1);
        String nome = segundo.getNome(), hash = segundo.getSenha();
        assertThrows(NegocioException.class, () -> controller.editar(segundo.getId(), "Rejeitado", primeiro.getCpf(),
                segundo.getTelefone(), segundo.getEmail(), segundo.getEndereco(), segundo.getSalario(),
                segundo.getCargo(), segundo.getStatus(), "nova-senha-teste"));
        em.clear();
        Funcionario recarregado = controller.buscarPorId(segundo.getId());
        assertEquals(nome, recarregado.getNome());
        assertEquals(hash, recarregado.getSenha());
    }

    @Test
    void editarSemSenhaMantemHashAtual() {
        FuncionarioController controller = new FuncionarioController(em);
        Funcionario funcionario = controller.autenticar("João Silva", "1234");
        String hash = funcionario.getSenha();
        controller.editar(funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getTelefone(),
                funcionario.getEmail(), funcionario.getEndereco(), funcionario.getSalario(), funcionario.getCargo(), funcionario.getStatus(), "");
        em.clear();
        assertEquals(hash, controller.buscarPorId(funcionario.getId()).getSenha());
    }

    @Test
    void novaSenhaSoValeAposEdicaoConfirmada() {
        FuncionarioController controller = new FuncionarioController(em);
        Funcionario funcionario = controller.autenticar("Maria Souza", "1234");
        assertNotNull(funcionario);
        try {
            controller.editar(funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getTelefone(),
                    funcionario.getEmail(), funcionario.getEndereco(), funcionario.getSalario(), funcionario.getCargo(), funcionario.getStatus(), "senha-teste-atualizada");
            em.clear();
            assertNotNull(controller.autenticar("Maria Souza", "senha-teste-atualizada"));
            assertNull(controller.autenticar("Maria Souza", "1234"));
        } finally {
            controller.editar(funcionario.getId(), funcionario.getNome(), funcionario.getCpf(), funcionario.getTelefone(),
                    funcionario.getEmail(), funcionario.getEndereco(), funcionario.getSalario(), funcionario.getCargo(), funcionario.getStatus(), "1234");
        }
    }

    @Test
    void cadastraClienteEServicoEPermiteExcluirSemVinculos() {
        ClienteController clientes = new ClienteController(em);
        ServicoController servicos = new ServicoController(em);
        clientes.salvar("Cliente teste CRUD", "529.982.247-25", "Rua teste", "45999999999", "crud@example.test", Status.ATIVO);
        Cliente cliente = clientes.listar().stream().filter(c -> c.getDocumento().equals("52998224725")).findFirst().orElseThrow();
        servicos.salvar("Serviço teste CRUD", "Teste", LocalDate.now().plusDays(1), 150, cliente, Status.AGENDADO);
        Servico servico = servicos.listar().stream().filter(s -> s.getNomeServico().equals("Serviço teste CRUD")).findFirst().orElseThrow();
        assertEquals(cliente.getId(), servico.getCliente().getId());
        servicos.excluir(servico.getId());
        clientes.excluir(cliente.getId());
        em.clear();
        assertNull(servicos.buscarPorId(servico.getId()));
        assertNull(clientes.buscarPorId(cliente.getId()));
    }

    @Test
    void servicoInvalidoNaoAlteraRegistroEEdicaoValidaFunciona() {
        ServicoController controller = new ServicoController(em);
        Servico servico = controller.listar().getFirst();
        double valor = servico.getValor();
        assertThrows(NegocioException.class, () -> controller.editar(servico.getId(), "Rejeitado", servico.getDescricao(),
                servico.getData(), -1, servico.getCliente(), servico.getStatus()));
        em.clear();
        assertEquals(valor, controller.buscarPorId(servico.getId()).getValor());
        controller.editar(servico.getId(), servico.getNomeServico(), servico.getDescricao(), LocalDate.now().plusDays(7),
                valor, controller.buscarPorId(servico.getId()).getCliente(), servico.getStatus());
        em.clear();
        assertEquals(LocalDate.now().plusDays(7), controller.buscarPorId(servico.getId()).getData());
    }
}
