package br.edu.uniamerica.projetomensal.menu;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ServicoMenu {

    // Instancia do ServicoService e ClienteService para realizar operacoes
    private ServicoService servicoService;
    private ClienteService clienteService;

    // Scanner para entrada de dados
    Scanner sc = new Scanner(System.in);

    int opcao;

    // Construtor que inicializa os services com EntityManager
    public ServicoMenu() {
        EntityManager em = PersistenceManager.getEntityManager();
        this.servicoService = new ServicoService(em);
        this.clienteService = new ClienteService(em);
    }

    // Metodo para iniciar o menu de servicos
    public void iniciar() {
        do {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| === === SISTEMA DETETIZADORA === === |");
            System.out.println("|  -- --- --- Menu Servico --- --- --  |");
            System.out.println("|                                      |");
            System.out.println("| 1 - Cadastar                         |");
            System.out.println("| 2 - Atualizar                        |");
            System.out.println("| 3 - Apagar                           |");
            System.out.println("| 4 - Listar                           |");
            System.out.println("| 0 - Voltar                           |");
            System.out.println("|--------------------------------------|");
            opcao = InputUtils.lerInt(sc,"Opcao: ");

            switch (opcao) {
                case 1:
                    cadastrarServico();
                    break;
                case 2:
                    editarServico();
                    break;
                case 3:
                    excluirServico();
                    break;
                case 4:
                    listarServico();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    // Metodo para cadastrar um novo servico
    void cadastrarServico() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|  --- --- Cadastro de Servico --- --- |");
        System.out.println("|--------------------------------------|");

        // Verificacao para criar servico apenas se o cliente estiver cadastrado
        List<Cliente> clientes = clienteService.listar();
        if (clientes.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum cliente cadastrado.           |");
            System.out.println("| Cadastre um cliente antes do servico.|");
            System.out.println("|--------------------------------------|");
            return;
        }

         // Utilizando a Classe InputUtils para ler os dados do servico com validacao
         String nomeServico = InputUtils.lerString(sc, "| Nome do Servico: ");
         String descricao = InputUtils.lerString(sc, "| Descricao do Servico: ");
         String dataString = InputUtils.lerData(sc, "| Data Agendada (DD/MM/AAAA): ");
         // Converter data no formato dd/MM/yyyy para LocalDate
         String[] dateParts = dataString.split("/");
         LocalDate data;
         try {
             data = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
         } catch (Exception e) {
             System.out.println("|--------------------------------------|");
             System.out.println("| Erro ao processar data.              |");
             System.out.println("| Voltando ao menu...                  |");
             System.out.println("|--------------------------------------|");
             return;
         }
         double valor = InputUtils.lerValorServico(sc, "| Valor do Servico: R$ ");

        System.out.println("| Clientes disponiveis ----------------|");
        for (Cliente c : clientes) {
            System.out.println("| ID: " + c.getId() + " - Nome: " + c.getNomeEmpresa());
        }
        Cliente cliente;
        while (true) {
            int clienteID = InputUtils.lerInt(sc, "| ID do Cliente: ");
            cliente = clienteService.buscarPorId(clienteID);
            if (cliente != null) {
                break; // Cliente encontrado, sai do loop
            } else {
                System.out.println("| Cliente nao encontrado. Tente novamente.");
            }
        }

        System.out.println("| Status do Servico ");
        System.out.println("| 1 - INATIVO\n| 2 - AGENDADO\n| 3 - EM ANDAMENTO\n| 4 - CONCLUIDO");
        int statusInput = InputUtils.lerInt(sc, "| Informe o numero do status: ");

        // Transforma o Enum Status a partir do numero informado pelo usuario, como uma lista de opcoes
        // Variavel Status do tipo Enum para armazenar o status selecionado

        Status status;
        if (statusInput > 4 || statusInput < 1) { // Verificacao para nao permitir 1 - ATIVO
            System.out.println("| Status invalido. Definindo como AGENDADO.");
            status = Status.AGENDADO;
        } else {
            // Cria um array de Status do Enum e seleciona o status com base na entrada do usuario
            // ex: [ATIVO, INATIVO, AGENDADO, EM_ANDAMENTO, CONCLUIDO]
            status = Status.values()[statusInput];
        }

        // Cria o objeto Servico utilizando o ServicoService e o metodo cadastrar, passando os dados informados pelo usuario
        Servico servico = servicoService.cadastrar(nomeServico, descricao, data, valor, cliente, status);

        System.out.println("\n|---------------------------------------|");
        System.out.println("| Servico cadastrado com sucesso!       |");
        System.out.println("| ID do Servico: " + servico.getId());
        System.out.println("|---------------------------------------|");
    }

    void listarServico() {

        // Cria uma lista dinamica utilizando o servicoService para listar todos os servicos cadastrados
        List<Servico> servicos = servicoService.listar();

        System.out.println("\n|--------------------------------------|");
        System.out.println("|  --- --- Lista de Servicos --- ---   |");
        System.out.println("|--------------------------------------|");

        // Verifica se a lista esta vazia
        if(servicos.isEmpty()){
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum servico cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Imprime os dados usando for-each, pegando os atributos e utilizando os getters
        for (Servico c : servicos) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| ID: " + c.getId());
            System.out.println("| Nome do Servico: " + c.getNomeServico());
            System.out.println("| Descricao: " + c.getDescricao());
            System.out.println("| Data: " + c.getData());
            System.out.println("| Valor: R$ " + c.getValor());
            System.out.println("| ID do Cliente: " + c.getCliente().getId());
            System.out.println("| Status: " + c.getStatus());
            System.out.println("|--------------------------------------|");
        }

        // Mostrando o total com metodo size() da lista
        System.out.println("\n| Total de servicos: " + servicos.size());
    }

    // Metodo para excluir um funcionario existente, solicitando o ID a ser removido
    void excluirServico() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|    --- --- Excluir Servico --- ---   |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Servicos disponiveis:                |");

        // Criando uma lista dinamica com servicoService para obter os funcionarios cadastrados
        List<Servico> servicos = servicoService.listar();

        // Verificando se a lista esta vazia, e se nao estiver, exibe os funcionarios disponiveis
        if (servicos.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum servico cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        } else {
            for (Servico c : servicos) {
                System.out.println("| ID: " + c.getId() + " - Nome: " + c.getNomeServico());
            }
        }

        int id = InputUtils.lerInt(sc, "| Informe o ID do servico: ");

        // Criando o objeto utilizando servicoService para buscar o servico pelo ID informado, utilizando o metodo buscarPorId
        Servico servico = servicoService.buscarPorId(id);

        // Verifica se existe o servico
        if(servico == null){
            System.out.println("|--------------------------------------|");
            System.out.println("| Servico nao encontrado.              |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Mostra o dado a ser excluido
        System.out.println("|--------------------------------------|");
        System.out.println("| Servico encontrado:");
        System.out.println("| Nome: " + servico.getNomeServico());
        System.out.println("| Valor: R$ " + servico.getValor());
        System.out.println("| Status: " + servico.getStatus());
        System.out.println("|--------------------------------------|");

        String confirmacao = InputUtils.lerString(sc,"| Confirmar exclusao (S/N): ");

        // Validacao com "equalsIgnoreCase" que ignora maiusculas e minusculas, caso usuario digite qualquer outra letra, a operacao sera cancelada
        if(confirmacao.equalsIgnoreCase("S")){
            servicoService.excluir(id);
            System.out.println("| Servico removido com sucesso.");
        } else {
            System.out.println("| Operacao cancelada.");
        }
    }

    // Metodo para editar um funcionario existente, solicitando o ID do funcionario a ser editado
    void editarServico() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|    --- --- Editar Servico --- ---    |");
        System.out.println("|--------------------------------------|");

        // Criando uma lista dinamica com servicoService para obter os servicos cadastrados
        List<Servico> servicos = servicoService.listar();
        // Verificando se a lista esta vazia, e se nao estiver, exibe os servicos disponiveis
        if (servicos.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum servico cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        } else {
            for (Servico c : servicos) { // Exibe o ID e o nome do servico para facilitar a escolha do servico a ser editado
                System.out.println("| ID: " + c.getId() + " - Nome: " + c.getNomeServico());
            }
        }

        int id = InputUtils.lerInt(sc, "| Informe o ID do servico: ");

        Servico servico = servicoService.buscarPorId(id);

        // Verifica se o ID digitado corresponde a um servico existente
        if(servico == null) {
            System.out.println("|--------------------------------------|");
            System.out.println("| ERRO! Servico nao encontrado.        |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Mostra os dados a serem editados, e solicita alteracao
        System.out.println("|--------------------------------------|");
        System.out.println("| Servico encontrado!                  |");
        System.out.println("| Nome do servico atual: " + servico.getNomeServico());
        // Utilizando "lerStringOpcional" para permitir enter
        String nomeServico = InputUtils.lerStringOpcional(sc, "| Novo nome do servico (ENTER p/ manter): ");
        if (!nomeServico.isEmpty()) { // Caso o usuario digite algo diferente de vazio, o nome do servico sera atualizado, caso contrario, mantem o nome atual
            servico.setNomeServico(nomeServico);
        }

         System.out.println("| Data atual: " + servico.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
         String dataString = InputUtils.lerDataOpcional(sc, "| Nova data (ENTER para manter): ");
         if (!dataString.isEmpty()) {
             try {
                 String[] dateParts = dataString.split("/");
                 LocalDate data = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                 servico.setData(data);
             } catch (Exception e) {
                 System.out.println("| Erro ao processar data. Mantendo data atual.");
             }
         }

        System.out.println("| Valor atual: R$ " + servico.getValor());
        double valor = InputUtils.lerDoubleOpcional(sc, "| Novo valor (0 p/ manter): R$ ");

        if (valor != 0 && valor != servico.getValor() && valor > 0) {
            servico.setValor(valor);
        } else if (valor < 0 || valor == servico.getValor()) {
            System.out.println("| Valor invalido. Mantendo valor atual. |");
        }

        System.out.println("| Status atual: " + servico.getStatus());
        System.out.println("| Novo Status");
        System.out.println("| 1 - INATIVO\n| 2 - AGENDADO\n| 3 - EM ANDAMENTO\n| 4 - CONCLUIDO");
        int statusInput = InputUtils.lerIntOpcional(sc, "| Informe o numero do status (ENTER para manter): ");

        // Verificacao para colocar os status, e proibir o status 1 - ATIVO
        if(statusInput != 0) {
            if (statusInput > 1 && statusInput <= 4) {
                Status status = Status.values()[statusInput];
                servico.setStatus(status);
            } else {
                System.out.println("| Status invalido. Mantendo status atual. |");
            }
        }

        // Edita o objeto funcionario utilizando o funcionarioService, e mostra mensagem de sucesso
        servicoService.editar(servico);

        System.out.println("|---------------------------------------|");
        System.out.println("| Servico atualizado com sucesso!       |");
        System.out.println("|---------------------------------------|");
    }
}
