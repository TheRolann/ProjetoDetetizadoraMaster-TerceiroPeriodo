package br.edu.uniamerica.projetomensal.config;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.utils.NegocioException;

public final class Sessao {
    private static volatile Funcionario usuario;
    private Sessao() {}
    public static void iniciar(Funcionario funcionario) { usuario = funcionario; }
    public static void encerrar() { usuario = null; }
    public static Funcionario usuario() { return usuario; }
    public static boolean gerente() { return usuario != null && usuario.getStatus() == Status.ATIVO && usuario.getCargo() == Cargo.GERENTE; }
    public static void exigirLogin() {
        if (usuario == null || usuario.getStatus() != Status.ATIVO) throw new NegocioException("Faça login para continuar.");
    }
    public static void exigirGerente() {
        exigirLogin();
        if (!gerente()) throw new NegocioException("Esta operação é exclusiva de gerente.");
    }
}
