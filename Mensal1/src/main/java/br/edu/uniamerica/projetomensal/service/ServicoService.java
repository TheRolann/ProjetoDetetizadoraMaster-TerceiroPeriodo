package br.edu.uniamerica.projetomensal.service;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.repository.ServicoRepository;
import br.edu.uniamerica.projetomensal.model.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Classe de servico para gerenciar as operacoes relacionadas aos servicos

public class ServicoService {

    // Instancia do repository para acessar os dados dos servicos
    private ServicoRepository repository = new ServicoRepository();

    // Construtor
    public Servico cadastrar(String nomeServico, String descricao, LocalDate data, double valor, Cliente cliente, Status status) {
        Servico servico = new Servico(nomeServico, descricao, data, valor, cliente, status);
        salvar(servico);

        return servico;
    }

    // Metodos Crud implementados para realizar as operacoes de salvar, excluir, editar, buscar por ID e listar servicos

    public void salvar(Servico servico) {
        try {
            // Validação do valor do serviço
            if (servico.getValor() <= 0) {
                throw new RuntimeException("O valor do serviço deve ser maior que 0");
            }

            repository.salvar(servico);
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluir(int id) {
        try {
            repository.excluir(id);
        } catch (Exception e) {
            System.out.println("Erro ao remover: " + e.getMessage());
        }
    }

    // Metodo de edicao que busca o cliente existente por ID e atualiza seus dados com os novos valores fornecidos
    public void editar(Servico servico) {
        try {
            Servico servicoExistente = buscarPorId(servico.getId());

            if (servicoExistente != null) {
                servicoExistente.setNomeServico(servico.getNomeServico());
                servicoExistente.setDescricao(servico.getDescricao());
                servicoExistente.setData(servico.getData());
                servicoExistente.setValor(servico.getValor());
                servicoExistente.setCliente(servico.getCliente());
                servicoExistente.setStatus(servico.getStatus());

                repository.editar(servicoExistente); // Salva no banco a edicao do servicos
            }
        } catch (Exception e) {
            System.out.println("Erro ao editar: " + e.getMessage());
        }
    }

    // Metodo para listar todos os servicos cadastrados, retornando uma lista de objetos servicos
    public Servico buscarPorId(int id) {
        try {
            return repository.buscarPorId(id);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por id: " + e.getMessage());
            return null;
        }
    }

    public List<Servico> listar() {
        try {
            return repository.listar();
        } catch (Exception e) {
            System.out.println("Erro ao listar: " + e.getMessage());
            return new ArrayList<>(); // Retornando lista vazia para nao crashar no MENU. NullPointerException
        }
    }
}