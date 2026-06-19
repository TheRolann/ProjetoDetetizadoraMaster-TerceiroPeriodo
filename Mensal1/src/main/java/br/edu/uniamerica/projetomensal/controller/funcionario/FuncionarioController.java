package br.edu.uniamerica.projetomensal.controller.funcionario;

import br.edu.uniamerica.projetomensal.model.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.model.entity.FuncionarioEntity;
import jakarta.persistence.EntityManager;

import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(EntityManager em){
        this.funcionarioService = new FuncionarioService(em);
    }

    public void salvarFuncionario(FuncionarioRequest funcionarioRequest){
        funcionarioService.salvar(funcionarioRequest);
    }

    public void editarFuncionario(int idFuncionario, FuncionarioRequest funcionarioRequest){
        funcionarioService.editar(idFuncionario, funcionarioRequest);
    }

    public void excluirFuncionario(int idFuncionario) {

        funcionarioService.excluir(idFuncionario);
    }

    public FuncionarioResponse buscarFuncionarioPorId(int id) {
        FuncionarioEntity entity = funcionarioService.buscarPorId(id);
        if (entity == null) return null;

        return new FuncionarioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getEmail(),
                entity.getEndereco(),
                entity.getSalario(),
                entity.getCargo(),
                entity.getStatus()
        );
    }

    public List<FuncionarioResponse> listarFuncionarios() {
        return funcionarioService.listar().stream()
                .map(entity -> new FuncionarioResponse(
                        entity.getId(),
                        entity.getNome(),
                        entity.getCpf(),
                        entity.getTelefone(),
                        entity.getEmail(),
                        entity.getEndereco(),
                        entity.getSalario(),
                        entity.getCargo(),
                        entity.getStatus()
                ))
                .toList();
    }
}