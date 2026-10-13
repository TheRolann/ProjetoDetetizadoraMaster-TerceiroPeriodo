package br.edu.uniamerica.projetomensal.utils;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;

// As mesmas regras valem ao criar e editar, mesmo fora das telas.
public final class ValidacaoCadastro {
    private ValidacaoCadastro() {}

    public static void cliente(Cliente cliente) {
        obrigatorio(cliente.getNomeEmpresa(), 150, "Nome do cliente");
        String documento = limparDocumento(cliente.getDocumento());
        if (!ValidacaoDocumentos.validar(documento)) throw new ErroCampo("documento", "CPF ou CNPJ inválido.");
        cliente.setDocumento(documento);
        cliente.setTelefone(telefone(cliente.getTelefone()));
        cliente.setEmail(email(cliente.getEmail()));
        cliente.setEndereco(opcional(cliente.getEndereco()));
        statusPessoa(cliente.getStatus());
    }

    public static void funcionario(Funcionario funcionario) {
        obrigatorio(funcionario.getNome(), 100, "Nome do funcionário");
        String cpf = limparDocumento(funcionario.getCpf());
        if (!ValidacaoDocumentos.validarCPF(cpf)) throw new ErroCampo("cpf", "Informe um CPF válido de 11 dígitos.");
        funcionario.setCpf(cpf);
        funcionario.setTelefone(telefone(funcionario.getTelefone()));
        funcionario.setEmail(email(funcionario.getEmail()));
        funcionario.setEndereco(opcional(funcionario.getEndereco()));
        if (!Double.isFinite(funcionario.getSalario()) || funcionario.getSalario() < 0 || funcionario.getSalario() > 99999999.99) {
            throw new ErroCampo("salario", "Informe um salário válido, maior ou igual a zero.");
        }
        if (funcionario.getCargo() == null) throw new ErroCampo("cargo", "Selecione o cargo.");
        statusPessoa(funcionario.getStatus());
    }

    public static void servico(Servico servico) {
        obrigatorio(servico.getNomeServico(), 100, "Nome do serviço");
        if (!Double.isFinite(servico.getValor()) || servico.getValor() <= 0 || servico.getValor() > 99999999.99) {
            throw new ErroCampo("valor", "Informe um valor válido e maior que zero.");
        }
        if (servico.getData() == null) throw new ErroCampo("data", "Informe a data do serviço.");
        if (servico.getCliente() == null || servico.getCliente().getId() <= 0) throw new ErroCampo("cliente", "Selecione um cliente cadastrado.");
        Status status = servico.getStatus();
        if (status != Status.AGENDADO && status != Status.EM_ANDAMENTO && status != Status.CONCLUIDO && status != Status.INATIVO) {
            throw new ErroCampo("status", "Selecione um status válido.");
        }
    }

    public static void senha(String senha) {
        if (senha == null || senha.isBlank()) throw new ErroCampo("senha", "Informe a senha para cadastrar o funcionário.");
        if (senha.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new ErroCampo("senha", "A senha é muito longa. Use até 72 bytes em UTF-8.");
        }
    }

    private static String limparDocumento(String documento) {
        return documento == null ? "" : documento.trim().replaceAll("[./-]", "");
    }

    private static void obrigatorio(String valor, int tamanho, String campo) {
        if (valor == null || valor.isBlank() || valor.length() > tamanho) {
            throw new ErroCampo("nome", campo + " é obrigatório e deve ter até " + tamanho + " caracteres.");
        }
    }

    private static String opcional(String texto) { return texto == null || texto.isBlank() ? null : texto.trim(); }

    private static String telefone(String texto) {
        String valor = opcional(texto);
        if (valor == null) return null;
        valor = valor.replaceAll("[()\\s.-]", "");
        if (!valor.matches("\\d{10,11}")) throw new ErroCampo("telefone", "Informe DDD e telefone com 10 ou 11 dígitos.");
        return valor;
    }

    private static String email(String texto) {
        String valor = opcional(texto);
        if (valor != null && (valor.length() > 100 || !valor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) {
            throw new ErroCampo("email", "Informe um e-mail como nome@dominio.com ou deixe vazio.");
        }
        return valor;
    }

    private static void statusPessoa(Status status) {
        if (status != Status.ATIVO && status != Status.INATIVO) throw new ErroCampo("status", "Selecione Ativo ou Inativo.");
    }
}
