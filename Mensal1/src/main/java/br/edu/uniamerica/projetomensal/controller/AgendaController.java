package br.edu.uniamerica.projetomensal.controller;

import br.edu.uniamerica.projetomensal.service.AgendaService;
import br.edu.uniamerica.projetomensal.model.*;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;

public class AgendaController {
    private final AgendaService service;
    public AgendaController(EntityManager em) { service = new AgendaService(em); }
    public List<Agenda> listar() { return service.listar(); }
    public List<Funcionario> responsaveis() { return service.responsaveis(); }
    public void salvar(int id, int servico, int funcionario, LocalDate data, LocalTime hora, String observacoes, Status status) { service.salvar(id, servico, funcionario, data, hora, observacoes, status); }
    public void cancelar(int id) { service.cancelar(id); }
}
