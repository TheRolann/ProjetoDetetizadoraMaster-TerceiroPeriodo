package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import jakarta.persistence.EntityManager;

public class RelatorioCliente {

    // Relatorio que exibe o total de clientes cadastrados e seus nomes
    public void totalClientes() {
        EntityManager em = PersistenceManager.getEntityManager();
        ClienteService clienteService = new ClienteService(em);
        int total = clienteService.listar().size();

        System.out.println("|--------------------------------------|");
        System.out.println("|  --- --- Relatorio Clientes --- ---  |");
        System.out.println("| Total de Clientes: " + total);
        System.out.println("|--------------------------------------|");

        for (Cliente c : clienteService.listar()) {
            System.out.printf("| %d | %s \n", c.getId(), c.getNomeEmpresa());
        }
        System.out.println("|--------------------------------------|");
    }
}
