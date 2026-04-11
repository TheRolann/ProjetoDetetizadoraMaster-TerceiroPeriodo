package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.service.ClienteService;

public class RelatorioCliente {

    // Relatorio que exibe o total de clientes cadastrados e seus nomes
    public void totalClientes() {
        // Instancia o ClienteService para acessar os dados dos clientes
        ClienteService clienteService = new ClienteService();
        int total = clienteService.listar().size(); // Obtem o total de clientes cadastrados

        System.out.println("|--------------------------------------|");
        System.out.println("|  --- --- Relatorio Clientes --- ---  |");
        System.out.println("| Total de Clientes: " + total);
        System.out.println("|--------------------------------------|");

        // Exibe os nomes e IDs dos clientes cadastrados
        for (Cliente c : clienteService.listar()) {
            System.out.printf("| %d | %s \n", c.getId(), c.getNomeEmpresa());
        }
        System.out.println("|--------------------------------------|");
    }
}
