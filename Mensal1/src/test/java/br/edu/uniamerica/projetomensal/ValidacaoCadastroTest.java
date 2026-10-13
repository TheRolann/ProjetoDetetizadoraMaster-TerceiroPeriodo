package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.*;
import br.edu.uniamerica.projetomensal.utils.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ValidacaoCadastroTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1", "11111111111", "5299822472A", "52998224724"})
    void documentosMalformadosNaoLancamErroInesperado(String cpf) {
        assertFalse(ValidacaoDocumentos.validarCPF(cpf));
        assertFalse(ValidacaoDocumentos.validar(cpf));
    }

    @Test
    void clienteAceitaDocumentoFormatadoERejeitaEmailInvalido() {
        Cliente cliente = new Cliente("Empresa teste", "11.222.333/0001-81", "Rua teste",
                "45999999999", "contato@example.test", Status.ATIVO);
        assertDoesNotThrow(() -> ValidacaoCadastro.cliente(cliente));
        assertEquals("11222333000181", cliente.getDocumento());
        cliente.setEmail("email-sem-dominio");
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.cliente(cliente));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY, 100000000})
    void rejeitaSalarioInvalido(double salario) {
        Funcionario funcionario = funcionario();
        funcionario.setSalario(salario);
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.funcionario(funcionario));
    }

    @Test
    void funcionarioNaoPodeUsarCnpjNemStatusDeServico() {
        Funcionario funcionario = funcionario();
        funcionario.setCpf("11222333000181");
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.funcionario(funcionario));
        funcionario.setCpf("52998224725");
        funcionario.setStatus(Status.AGENDADO);
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.funcionario(funcionario));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, Double.NaN, Double.POSITIVE_INFINITY, 100000000})
    void rejeitaValorDeServicoInvalido(double valor) {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        Servico servico = new Servico("Controle de pragas", "Teste", LocalDate.now(), valor, cliente, Status.AGENDADO);
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.servico(servico));
    }

    @Test
    void senhaNaoPodeSerVaziaOuExcederLimiteDoBcrypt() {
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.senha(" "));
        assertThrows(NegocioException.class, () -> ValidacaoCadastro.senha("á".repeat(37)));
        assertDoesNotThrow(() -> ValidacaoCadastro.senha("senha-teste"));
    }

    private Funcionario funcionario() {
        return new Funcionario("Teste", "52998224725", "45999999999", "teste@example.test",
                "Rua teste", 2500, Cargo.FUNCIONARIO, Status.ATIVO);
    }

    @Test
    void contatoOpcionalVazioENuloPodeSerSalvo() {
        Cliente cliente=new Cliente("Teste","11222333000181"," ","",null,Status.ATIVO);
        assertDoesNotThrow(() -> ValidacaoCadastro.cliente(cliente));
        assertNull(cliente.getEmail()); assertNull(cliente.getTelefone()); assertNull(cliente.getEndereco());
        Funcionario funcionario=funcionario(); funcionario.setTelefone(null); funcionario.setEmail(" ");
        assertDoesNotThrow(() -> ValidacaoCadastro.funcionario(funcionario));
    }
    @Test
    void telefoneFormatadoNormalizaEContatoPreenchidoInvalidoIdentificaCampo() {
        Funcionario funcionario=funcionario(); funcionario.setTelefone("(45) 99999-9999");
        ValidacaoCadastro.funcionario(funcionario); assertEquals("45999999999",funcionario.getTelefone());
        funcionario.setTelefone("123");
        assertEquals("telefone",assertThrows(ErroCampo.class,()->ValidacaoCadastro.funcionario(funcionario)).campo());
        funcionario.setTelefone(null); funcionario.setEmail("sem-arroba");
        assertEquals("email",assertThrows(ErroCampo.class,()->ValidacaoCadastro.funcionario(funcionario)).campo());
    }
}
