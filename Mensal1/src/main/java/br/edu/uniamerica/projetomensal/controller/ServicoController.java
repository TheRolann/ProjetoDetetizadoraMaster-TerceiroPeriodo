package br.edu.uniamerica.projetomensal.controller;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.utils.NegocioException;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

// Controller recebe as acoes da View e delega ao Service
// Nao conhece Swing, nao importa nada de javax.swing
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(EntityManager em) {
        this.servicoService = new ServicoService(em);
    }

    public void salvar(String nomeServico, String descricao, LocalDate data,
                       double valor, Cliente cliente, Status status) {
        servicoService.cadastrar(nomeServico, descricao, data, valor, cliente, status);
    }

    public void editar(int id, String nomeServico, String descricao, LocalDate data,
                       double valor, Cliente cliente, Status status) {
        Servico servico = servicoService.buscarPorId(id);
        if (servico == null) throw new NegocioException("Servico não encontrado");

        servico.setNomeServico(nomeServico);
        servico.setDescricao(descricao);
        servico.setData(data);
        servico.setValor(valor);
        servico.setCliente(cliente);
        servico.setStatus(status);

        servicoService.editar(servico);
    }

    public void excluir(int id) {
        servicoService.excluir(id);
    }

    public Servico buscarPorId(int id) {
        return servicoService.buscarPorId(id);
    }

    public List<Servico> listar() {
        return servicoService.listar();
    }
}