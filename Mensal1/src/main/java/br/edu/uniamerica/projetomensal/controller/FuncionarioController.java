package br.edu.uniamerica.projetomensal.controller;

import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import br.edu.uniamerica.projetomensal.utils.ValidacaoCadastro;
import java.util.List;

// Controller recebe as acoes da View e delega ao Service
// Nao conhece Swing, nao importa nada de javax.swing
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(EntityManager em) {
        this.funcionarioService = new FuncionarioService(em);
    }

    public void salvar(String nome, String cpf, String telefone, String email,
                       String endereco, double salario, Cargo cargo, String senha) {
        Funcionario funcionario = new Funcionario(
                nome, cpf, telefone, email, endereco, salario, cargo, Status.ATIVO
        );
        ValidacaoCadastro.senha(senha);
        funcionario.setSenha(senha);
        funcionarioService.salvar(funcionario);
    }

    public void editar(int id, String nome, String cpf, String telefone, String email,
                       String endereco, double salario, Cargo cargo, Status status, String senha) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(id);

        funcionario.setNome(nome);
        funcionario.setCpf(cpf);
        funcionario.setTelefone(telefone);
        funcionario.setEmail(email);
        funcionario.setEndereco(endereco);
        funcionario.setSalario(salario);
        funcionario.setCargo(cargo);
        funcionario.setStatus(status);

        // Campo vazio mantém a senha; o service copia um novo hash apenas após validar.
        if (senha != null && !senha.isEmpty()) {
            ValidacaoCadastro.senha(senha);
            funcionario.setSenha(senha);
        }

        funcionarioService.editar(funcionario);
    }

    public void excluir(int id) {
        funcionarioService.excluir(id);
    }

    public Funcionario buscarPorId(int id) {
        return funcionarioService.buscarPorId(id);
    }

    public List<Funcionario> listar() {
        return funcionarioService.listar();
    }

    public Funcionario autenticar(String nome, String senha) {
        return funcionarioService.autenticar(nome, senha);
    }
}
