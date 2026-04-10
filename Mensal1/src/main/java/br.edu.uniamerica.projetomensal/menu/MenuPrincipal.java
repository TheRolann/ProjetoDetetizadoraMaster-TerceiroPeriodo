package br.edu.uniamerica.projetomensal.menu;

import br.edu.uniamerica.projetomensal.utils.InputUtils;

import java.util.Scanner;

public class MenuPrincipal {
    Scanner sc = new Scanner(System.in);

    ClienteMenu clienteMenu = new ClienteMenu();
    FuncionarioMenu funcionarioMenu = new FuncionarioMenu();
    ServicoMenu servicoMenu = new ServicoMenu();
    AgendaMenu agendaMenu = new AgendaMenu();
    RelatorioMenu relatorioMenu = new RelatorioMenu();

    public void iniciar() {
        int opcao;

        do {
            System.out.println("|--------------------------------------|");
            System.out.println("| === === SISTEMA DETETIZADORA === === |");
            System.out.println("| -- --- --- Menu Principal --- --- -- |");
            System.out.println("|                                      |");
            System.out.println("| 1 - Clientes                         |");
            System.out.println("| 2 - Funcionarios                     |");
            System.out.println("| 3 - Servicos                         |");
            System.out.println("| 4 - Agendamentos                     |");
            System.out.println("| 5 - Relatorios                       |");
            System.out.println("| 0 - Sair                             |");
            System.out.println("|--------------------------------------|");
            opcao = InputUtils.lerInt(sc, "Opcao: ");

            switch (opcao) {
                case 1:
                    clienteMenu.iniciar();
                    break;
                case 2:
                    funcionarioMenu.iniciar();
                    break;
                case 3:
                    servicoMenu.iniciar();
                    break;
                case 4:
                    agendaMenu.iniciar();
                    break;
                case 5:
                    relatorioMenu.iniciar();
                    break;
                case 0:
                    System.out.println("Saindo do sistema.");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }
}