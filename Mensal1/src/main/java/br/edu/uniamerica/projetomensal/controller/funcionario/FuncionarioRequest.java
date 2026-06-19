package br.edu.uniamerica.projetomensal.controller.funcionario;

import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;

public record FuncionarioRequest(
        String nome,
        String cpf,
        String senha,
        String telefone,
        String email,
        String endereco,
        double salario,
        Cargo cargo,
        Status status
) {}
