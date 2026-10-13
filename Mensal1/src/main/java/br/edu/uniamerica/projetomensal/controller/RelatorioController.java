package br.edu.uniamerica.projetomensal.controller;
import br.edu.uniamerica.projetomensal.service.RelatorioService;
import jakarta.persistence.EntityManager;
public class RelatorioController {
    private final RelatorioService service;
    public RelatorioController(EntityManager em) { service=new RelatorioService(em); }
    public RelatorioService.Dados gerar(RelatorioService.Tipo tipo) { return service.gerar(tipo); }
}
