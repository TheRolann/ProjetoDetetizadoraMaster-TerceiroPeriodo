package br.edu.uniamerica.projetomensal.menu;

import br.edu.uniamerica.projetomensal.model.Cliente;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ClienteService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;

import java.util.List;
import java.util.Scanner;

public class ClienteMenu {

    // Instancia do servico de cliente para realizar as operacoes
    private ClienteService clienteService = new ClienteService();

    // Scanner para entrada de dados do usuario
    Scanner sc = new Scanner(System.in);

    int opcao;

    // Metodo para iniciar o menu de cliente
    public void iniciar() {
        do {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| === === SISTEMA DETETIZADORA === === |");
            System.out.println("|  -- --- --- Menu Cliente --- --- --  |");
            System.out.println("|                                      |");
            System.out.println("| 1 - Cadastar                         |");
            System.out.println("| 2 - Atualizar                        |");
            System.out.println("| 3 - Apagar                           |");
            System.out.println("| 4 - Listar                           |");
            System.out.println("| 0 - Voltar                           |");
            System.out.println("|--------------------------------------|");
            opcao = InputUtils.lerInt(sc, "Opcao: ");

            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    editarCliente();
                    break;
                case 3:
                    excluirCliente();
                    break;
                case 4:
                    listarCliente();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    // Metodo para cadastrar um novo cliente
    void cadastrarCliente() {

        System.out.println("\n|--------------------------------------|");
        System.out.println("|  --- --- Cadastro de Cliente --- --- |");
        System.out.println("|--------------------------------------|");

        // Utilizando a Classe InputUtils para ler os dados do cliente com validacao
        String nomeEmpresa = InputUtils.lerString(sc, "| Nome da Empresa/Cliente: ");
        String documento = InputUtils.lerDocumento(sc, "| Documento (CPF/CNPJ): ");
        String endereco = InputUtils.lerString(sc, "| Endereco: ");
        String telefone = InputUtils.lerTelefone(sc, "| Telefone: ");
        String email = InputUtils.lerEmail(sc, "| Email: ");

        System.out.print("| Status\n| 1 - ATIVO\n| 2 - INATIVO\n| Informe o numero do status: ");
        int statusInput = InputUtils.lerInt(sc, "");

        // Transforma o Enum Status a partir do numero informado pelo usuario, como uma lista de opcoes
        // ex: [ATIVO, INATIVO, AGENDADO, EM_ANDAMENTO, CONCLUIDO]
        Status status;
        if (statusInput > 2 || statusInput < 1) {
            System.out.println("| Status invalido. Definindo como ATIVO.");
            status = Status.ATIVO;
        } else {
            // Mesmo processo do cargo, converte o numero para o valor do Enum correspondente
            status = Status.values()[statusInput - 1];
        }

        // Criando o objeto cliente e cadastrando utilizando o clienteService, que gera o ID automaticamente
        Cliente cliente = clienteService.cadastrarCliente(nomeEmpresa, documento, endereco, telefone, email, status);
        System.out.println("|---------------------------------------|");
        System.out.println("| Cliente cadastrado com sucesso!       |");
        System.out.println("| ID do Cliente: " + cliente.getId());
        System.out.println("|---------------------------------------|");
    }

    // Metodo para listar todos os clientes cadastrados
    void listarCliente() {

        // Cria uma lista dinamica utilizando o clienteService para obter os clientes cadastrados
        List<Cliente> clientes = clienteService.listar();

        System.out.println("\n|--------------------------------------|");
        System.out.println("|  --- --- Lista de Clientes --- ---   |");
        System.out.println("|--------------------------------------|");

        // Verifica se a lista esta vazia
        if(clientes.isEmpty()){
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum cliente cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Imprime os dados usando for-each, pegando os atributos de cada cliente utilizando os getters
        for (Cliente c : clientes) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| ID: " + c.getId());
            System.out.println("| Nome da Empresa/Cliente: " + c.getNomeEmpresa());
            System.out.println("| Documento: " + c.getDocumento());
            System.out.println("| Endereço: " + c.getEndereco());
            System.out.println("| Telefone: " + c.getTelefone());
            System.out.println("| Email: " + c.getEmail());
            System.out.println("| Status: " + c.getStatus());
            System.out.println("|--------------------------------------|");
        }

        // Mostrando o total com metodo size() da lista
        System.out.println("\n| Total de clientes: " + clientes.size());
    }

    // Metodo para excluir um cliente existente, solicitando o ID do cliente a ser removido
    void excluirCliente() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|    --- --- Excluir Cliente --- ---   |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Clientes disponiveis:                |");

        // Criando uma lista dinamica com clienteService para obter os clientes cadastrados
        List<Cliente> clientes = clienteService.listar();

        // Verificando se a lista esta vazia, e se nao estiver, exibe os clientes disponiveis
        if (clientes.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum cliente cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        } else {
            for (Cliente c : clientes) {
                System.out.println("| ID: " + c.getId() + " - Nome: " + c.getNomeEmpresa());
            }
        }
        int id = InputUtils.lerInt(sc, "| Informe o ID do cliente: ");

        // Criando o objeto utilizando clienteService para buscar o cliente pelo ID informado, utilizando o metodo buscarPorId
        Cliente cliente = clienteService.buscarPorId(id);

        // Verificando se existe o cliente
        if(cliente == null){
            System.out.println("|--------------------------------------|");
            System.out.println("| Cliente nao encontrado.              |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Mostra o dado do cliente a ser excluido
        System.out.println("|--------------------------------------|");
        System.out.println("| Cliente encontrado:");
        System.out.println("| Nome: " + cliente.getNomeEmpresa());
        System.out.println("| Documento: " + cliente.getDocumento());
        System.out.println("| Status: " + cliente.getStatus());
        System.out.println("|--------------------------------------|");

        String confirmacao = InputUtils.lerString(sc,"| Confirmar exclusao (S/N): ");

        // Validacao com "equalsIgnoreCase" que ignora maiusculas e minusculas, caso usuario digite qualquer outra letra, a operacao sera cancelada
        if(confirmacao.equalsIgnoreCase("S")){
            clienteService.excluir(id);
            System.out.println("| Cliente removido com sucesso.");
        } else {
            System.out.println("| Operacao cancelada.");
        }
    }

    // Metodo para editar um cliente existente, solicitando o ID do cliente a ser editado
    void editarCliente() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|   --- --- Atualizar Cliente --- ---  |");
        System.out.println("|--------------------------------------|");

        // Criando uma lista dinamica com clienteService para obter os clientes cadastrados
        List<Cliente> clientes = clienteService.listar();
        // Verificando se a lista esta vazia, e se nao estiver, exibe os clientes disponiveis
        if (clientes.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum cliente cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        } else {
            for (Cliente c : clientes) { // Exibe o ID e o nome da empresa/cliente para facilitar a escolha do cliente a ser editado
                System.out.println("| ID: " + c.getId() + " - Nome: " + c.getNomeEmpresa());
            }
        }
        int id = InputUtils.lerInt(sc, "| Informe o ID do cliente: ");

        Cliente cliente = clienteService.buscarPorId(id);

        // Verifica se o ID digitado corresponde a um cliente existente
        if(cliente == null) {
            System.out.println("|--------------------------------------|");
            System.out.println("| ERRO! Cliente nao encontrado.        |");
            System.out.println("|--------------------------------------|");
            return;
        }

        // Mostra os dados do cliente a ser editado, e solicita alteracao
        System.out.println("|--------------------------------------|");
        System.out.println("| Cliente encontrado!                  |");
        System.out.println("| Nome da empresa/cliente atual: " + cliente.getNomeEmpresa());
        // Utilizando "lerStringOpcional" para permitir enter vazio
        String nomeEmpresa = InputUtils.lerStringOpcional(sc, "| Novo nome da empresa/cliente (ENTER p/ manter): ");
        if (!nomeEmpresa.isEmpty()) { // Caso o usuario digite algo diferente de vazio, o nome da empresa/cliente sera atualizado, caso contrario, mantem o nome atual
            cliente.setNomeEmpresa(nomeEmpresa);
        }

        System.out.println("| Telefone atual: " + cliente.getTelefone());
        String telefone = InputUtils.lerTelefoneOpcional(sc, "| Novo telefone (ENTER para manter): ");
        if (!telefone.isEmpty()) {
            cliente.setTelefone(telefone);
        }

        System.out.println("| Email atual: " + cliente.getEmail());
        String email = InputUtils.lerEmailOpcional(sc, "| Novo e-mail (ENTER para manter): ");
        if (!email.isEmpty()) {
            cliente.setEmail(email);
        }

        System.out.println("| Status atual: " + cliente.getStatus());
        System.out.print("| Novo status\n| 1 - ATIVO\n| 2 - INATIVO\n");
        int statusInput = InputUtils.lerIntOpcional(sc, "| Informe o numero do status (ENTER para manter): ");

        // Verificacao para colocar apenas os status ATIVO e INATIVO, para nao acessar outros status do enum

        if (statusInput != 0) {
            if (statusInput >= 1 && statusInput <= 2) {
                Status status = Status.values()[statusInput - 1];
                cliente.setStatus(status);
            } else {
                System.out.println("| Status invalido. Mantendo status atual. |");
            }
        }

        System.out.println("| Documento atual: " + cliente.getDocumento());
        String documento = InputUtils.lerStringOpcional(sc, "| Novo documento (ENTER para manter): ");
        if (!documento.isEmpty()) {
            String documentoLimpo = documento.replaceAll("[./-]", "");
            if (documentoLimpo.matches("\\d{11}|\\d{14}")) {
                cliente.setDocumento(documentoLimpo);
            } else {
                System.out.println("| Documento invalido. Mantendo documento atual.");
            }
        }

        // Edita o objeto cliente utilizando o clienteService, e mostra mensagem de sucesso
        clienteService.editar(cliente);

        System.out.println("|---------------------------------------|");
        System.out.println("| Cliente atualizado com sucesso!       |");
        System.out.println("|---------------------------------------|");
    }
}
