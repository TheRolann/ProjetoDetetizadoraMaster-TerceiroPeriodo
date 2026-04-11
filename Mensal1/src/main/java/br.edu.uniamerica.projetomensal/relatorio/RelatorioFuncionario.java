package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.service.FuncionarioService;
import jakarta.persistence.EntityManager;

public class RelatorioFuncionario {
    private EntityManager em = PersistenceManager.getEntityManager();
    // Instancia do FuncionarioService para acessar os dados dos funcionarios
    private FuncionarioService funcionarioService = new FuncionarioService(em);

    // Relatorio que exibe o total de funcionarios, gerentes e o total de salarios a pagar
    public void relatorioFuncionarios() {
        // Atributos
        int totalFuncionarios = 0;
        int totalGerentes = 0;

        double totalSalarios = 0;

        // For each para percorrer a lista de funcionarios e contar o total de cada cargo e somar o total de salarios
        for (Funcionario f : funcionarioService.listar()) {
            if (f.getCargo() == Cargo.GERENTE) {
                totalGerentes++;
            } else if (f.getCargo() == Cargo.FUNCIONARIO) {
                totalFuncionarios++;
            }

            // Somatorio
            totalSalarios += f.getSalario();
        }

        // Total dos funcionarios
        int total = totalFuncionarios + totalGerentes;

        System.out.println("\n|----------------------------------------|");
        System.out.println("|  --- --- Relatorio Funcionario --- --- |");
        System.out.println("|----------------------------------------|");
        System.out.printf("| Funcionarios: %d", totalFuncionarios);
        System.out.printf("\n| Gerentes: %d    \n", totalGerentes);
        System.out.println("|----------------------------------------|");
        System.out.printf("| Total de funcionarios: %d ", total);
        System.out.printf("\n| Total de salarios a pagar: R$ %.2f\n", totalSalarios);
        System.out.println("\n|----------------------------------------|");
    }
}
