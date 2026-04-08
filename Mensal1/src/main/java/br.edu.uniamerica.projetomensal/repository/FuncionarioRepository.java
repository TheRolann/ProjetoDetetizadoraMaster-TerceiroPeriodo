package br.edu.uniamerica.projetomensal.repository;

import br.edu.uniamerica.projetomensal.interfaces.Crud;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import java.util.ArrayList;
import java.util.List;

// Classe repository, que implementa a interface Crud, e é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) para a entidade Funcionario
public class FuncionarioRepository implements Crud<Funcionario> {
    // Crianco o "banco de dados" em memoria
    // Criando a List do tipo Funcionario
    private static List<Funcionario> funcionarios  = new ArrayList<>();

    // Sobreescrita dos metodos da interface Crud, para realizar as operacoes

    @Override
    public void salvar(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    // Apaga o funcionario da lista, caso o id seja encontrado, pelo numero do id, utilizando o metodo buscarPorId para encontrar
    @Override
    public void excluir(int id) {
        Funcionario funcionario = buscarPorId(id);
        if (funcionario != null) {
            funcionarios.remove(funcionario);
        }
    }

    // Editar vazio, pois a edicao do funcionario pode ser feita diretamente na lista, utilizando o metodo buscarPorId para encontrar o funcionario e editar seus atributos
    @Override
    public void editar(Funcionario funcionario) {}

    // Busca o funcionario na lista por id com for each, caso nao tenha, retorna null
    @Override
    public Funcionario buscarPorId(int id) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId() == id) {
                return funcionario;
            }
        }
        return null;
    }

    // Lista todos os funcionarios da lista, utilizando o metodo listar para retornar a lista de funcionarios
    public List<Funcionario> listar() {
        return funcionarios;
    }
}
