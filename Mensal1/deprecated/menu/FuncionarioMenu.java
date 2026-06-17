package br.edu.uniamerica.projetomensal.menu;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.model.Funcionario;
import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.model.service.FuncionarioService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Scanner;

public class FuncionarioMenu {
    private EntityManager em = PersistenceManager.getEntityManager();
    // Instancia do servico para gerenciar funcionarios
    private FuncionarioService funcionarioService = new FuncionarioService(em);
    Scanner sc = new Scanner(System.in);

    int opcao;

    // Metodo para iniciar o menu de funcionarios
    public void iniciar() {

        do {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| === === SISTEMA DETETIZADORA === === |");
            System.out.println("|  --- --- Menu Funcionarios  --- ---  |");
            System.out.println("|                                      |");
            System.out.println("| 1 - Cadastrar                        |");
            System.out.println("| 2 - Atualizar                        |");
            System.out.println("| 3 - Apagar                           |");
            System.out.println("| 4 - Listar                           |");
            System.out.println("| 0 - Voltar                           |");
            System.out.println("|--------------------------------------|");

            opcao = InputUtils.lerInt(sc,"Opcao: ");

            switch (opcao) {
                case 1:
                    cadastrarFuncionario();
                    break;
                case 2:
                    editarFuncionario();
                    break;
                case 3:
                    excluirFuncionario();
                    break;
                case 4:
                    listarFuncionarios();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    // Metodo para cadastrar um novo funcionario
    void cadastrarFuncionario() {
        try {

            System.out.println("\n|--------------------------------------|");
            System.out.println("|  - --- Cadastro de Funcionario --- - |");
            System.out.println("|--------------------------------------|");

            // Utilizando a Classe InputUtils para ler os dados do funcionario com validacao
            String nome = InputUtils.lerString(sc,"| Nome: ");
            String cpf = InputUtils.lerDocumento(sc,"| CPF/CNPJ: ");
            String telefone = InputUtils.lerTelefone(sc,"| Telefone: ");
            String email = InputUtils.lerEmail(sc,"| Email: ");
            String endereco = InputUtils.lerStringOpcional(sc,"| Endereco (opcional): ");
            double salario = InputUtils.lerDouble(sc,"| Salario: R$ ");

            System.out.println("| Cargo:");
            System.out.println("| 1 - GERENTE");
            System.out.println("| 2 - FUNCIONARIO");

            int cargoInput = InputUtils.lerInt(sc,"| Informe o numero do cargo: ");

            // Transforma o Enum em um valor do tipo Cargo
            Cargo cargo;
            if (cargoInput >= 1 && cargoInput <= 2) {
                cargo = Cargo.values()[cargoInput - 1];
            } else {
                System.out.println("| Cargo inválido. Definindo como FUNCIONARIO.");
                cargo = Cargo.FUNCIONARIO;
            }

            // Cria o objeto Funcionario utilizando o servico, que ja gera o ID automaticamente
            Funcionario funcionario = new Funcionario(nome, cpf, telefone, email, endereco, salario, cargo, Status.ATIVO);
            funcionarioService.salvar(funcionario);

            System.out.println("|--------------------------------------|");
            System.out.println("| Funcionario cadastrado com sucesso!  |");
            System.out.println("| ID: " + funcionario.getId());
            System.out.println("|--------------------------------------|");
        }catch (Exception e) {
            System.out.println("Erro ao cadastrar funcionarios: " + e.getMessage());
        }
    }

    // Metodo para listar todos os funcionarios cadastrados
    void listarFuncionarios() {
        try {

            // Cria uma lista de funcionarios utilizando o servico para obter todos os funcionarios cadastrados
            List<Funcionario> funcionarios = funcionarioService.listar();

            System.out.println("\n|--------------------------------------|");
            System.out.println("|  -- --- Lista de Funcionarios --- -- |");
            System.out.println("|--------------------------------------|");

            // Verifica se a lista esta vazia
            if(funcionarios.isEmpty()){
                System.out.println("\n|--------------------------------------|");
                System.out.println("| Nenhum funcionario cadastrado.       |");
                System.out.println("|--------------------------------------|");
                return;
            }

            // Imprime os dados usando for-each, pegando os atributos de cada funcionario utilizando os getters
            for (Funcionario f : funcionarios) {
                System.out.println("\n|--------------------------------------|");
                System.out.println("| ID: " + f.getId());
                System.out.println("| Nome: " + f.getNome());
                System.out.println("| CPF: " + f.getCpf());
                System.out.println("| Telefone: " + f.getTelefone());
                System.out.println("| Email: " + f.getEmail());
                System.out.println("| Cargo: " + f.getCargo());
                System.out.println("| Salario: R$ " + f.getSalario());
                System.out.println("|--------------------------------------|");
            }

            // Mostra o total de funcionarios cadastrados utilizando o metodo size() da lista
            System.out.println("\n| Total de funcionarios: " + funcionarios.size());
        }catch (Exception e) {
            System.out.println("Erro ao listar funcionarios: " + e.getMessage());
        }
    }

    // Metodo para excluir um funcionario existente, solicitando o ID do funcionario a ser removido
    void excluirFuncionario() {
        try {

            System.out.println("\n|--------------------------------------|");
            System.out.println("|  --- --- Remover Funcionario --- --- |");
            System.out.println("|--------------------------------------|");
            System.out.println("| Funcionarios disponiveis:            |");

            // Criando uma lista dinamica com funcionarioService para obter os funcionarios cadastrados
            List<Funcionario> funcionarios = funcionarioService.listar();

            // Verificando se a lista esta vazia, e se nao estiver, exibe os funcionarios disponiveis
            if(funcionarios.isEmpty()){
                System.out.println("\n|--------------------------------------|");
                System.out.println("| Nenhum funcionario cadastrado.       |");
                System.out.println("|--------------------------------------|");
                return;
            } else {
                for (Funcionario f : funcionarios) {
                    System.out.println("| ID: " + f.getId() + " - Nome: " + f.getNome());
                }
             }
             int id = InputUtils.lerInt(sc,"| Informe o ID do funcionario: ");
             if (id == 0) {
                 System.out.println("| ID inválido.");
                 return;
             }
             // Criando o objeto utilizando funcionarioService para buscar o funcionario pelo ID informado, utilizando o metodo buscarPorId()
             Funcionario funcionario = funcionarioService.buscarPorId(id);

            if(funcionario == null){
                System.out.println("|--------------------------------------|");
                System.out.println("| Funcionario nao encontrado.          |");
                System.out.println("|--------------------------------------|");
                return;
            }

            // Mostra os dados do funcionario a ser excluido
            System.out.println("|--------------------------------------|");
            System.out.println("| Funcionario encontrado:");
            System.out.println("| Nome: " + funcionario.getNome());
            System.out.println("| CPF: " + funcionario.getCpf());
            System.out.println("| Salario: R$ " + funcionario.getSalario());
            System.out.println("| Cargo: " + funcionario.getCargo());
            System.out.println("|--------------------------------------|");

            String confirmacao = InputUtils.lerString(sc,"| Confirmar exclusao (S/N): ");

            // Validacao com "equalsIgnoreCase" que ignora maiusculas e minusculas, caso usuario digite qualquer outra letra, a operacao sera cancelada
            if(confirmacao.equalsIgnoreCase("S")){
                funcionarioService.excluir(id);
                System.out.println("| Funcionario removido com sucesso.");
            } else {
                System.out.println("| Operacao cancelada.");
            }
        }catch (Exception e) {
            System.out.println("Erro ao remover funcionarios: " + e.getMessage());
        }
    }

    // Metodo para editar um funcionario existente, solicitando o ID do funcionario a ser editado
    void editarFuncionario() {
        try {
            System.out.println("\n|--------------------------------------|");
            System.out.println("|  -- --- Atualizar Funcionario --- -- |");
            System.out.println("|--------------------------------------|");

            // Criando uma lista dinamica com funcionarioService para obter os funcionarios cadastrados
            List<Funcionario> funcionarios = funcionarioService.listar();
            // Verificando se a lista esta vazia, e se nao estiver, exibe os funcionarios disponiveis
            if (funcionarios.isEmpty()) {
                System.out.println("\n|--------------------------------------|");
                System.out.println("| Nenhum funcionario cadastrado.       |");
                System.out.println("|--------------------------------------|");
                return;
             } else {
                 for (Funcionario f : funcionarios) { // Exibe o ID e o nome do funcionario para facilitar a escolha
                     System.out.println("| ID: " + f.getId() + " - Nome: " + f.getNome());
                 }
             }
             int id = InputUtils.lerInt(sc,"| Informe o ID do funcionario: ");
             if (id == 0) {
                 System.out.println("| ID inválido.");
                 return;
             }
             Funcionario funcionario = funcionarioService.buscarPorId(id);

            // Verifica se o ID digitado corresponde a um funcionario existente
            if(funcionario == null){
                System.out.println("|--------------------------------------|");
                System.out.println("| ERRO! Funcionario nao encontrado.    |");
                System.out.println("|--------------------------------------|");
                return;
            }

            // Mostra os dados do funcionario a ser editado, e solicita alteracao
            System.out.println("|--------------------------------------|");
            System.out.println("| Funcionario encontrado!              |");
            System.out.println("| Nome atual: " + funcionario.getNome());
            // Utilizando "lerStringOpcional" para permitir enter vazio
            String nome = InputUtils.lerStringOpcional(sc,"| Novo nome (ENTER p/ manter): ");
            if(!nome.isEmpty()) { // Caso o usuario digite algo diferente de vazio, o nome da empresa/cliente sera atualizado, caso contrario, mantem o nome atual
                funcionario.setNome(nome);
            }

            System.out.println("| Telefone atual: " + funcionario.getTelefone());
            String telefone = InputUtils.lerTelefoneOpcional(sc,"| Novo telefone (ENTER p/ manter): ");
            if(!telefone.isEmpty()) {
                funcionario.setTelefone(telefone);
            }

            System.out.println("| Email atual: " + funcionario.getEmail());
            String email = InputUtils.lerEmailOpcional(sc,"| Novo email (ENTER p/ manter): ");
            if(!email.isEmpty()) {
                funcionario.setEmail(email);
            }

            System.out.println("| Salario atual: R$ " + funcionario.getSalario());
            double salario = InputUtils.lerDoubleOpcional(sc,"| Novo salario (0 p/ manter): ");

            if(salario > 0){
                funcionario.setSalario(salario);
            }

            System.out.println("| Cargo atual: " + funcionario.getCargo());
            System.out.println("| 1 - GERENTE\n2 - FUNCIONARIO");
            int cargoInput = InputUtils.lerIntOpcional(sc,"| Informe o numero do cargo (ENTER para manter): ");

            // Verifica se o numero do cargo esta dentro do range permitido, se nao estiver, a operacao sera cancelada
            // Caso contrario, converte o numero para o valor do Enum correspondente e atualiza o cargo do funcionario

            if(cargoInput != 0) {
                if (cargoInput >= 1 && cargoInput <= 2) {
                    Cargo cargo = Cargo.values()[cargoInput - 1];
                    funcionario.setCargo(cargo);
                } else {
                    System.out.println("| Cargo invalido. Mantendo cargo atual. |");
                }
            }

            System.out.println("| Status atual: " + funcionario.getStatus());
            System.out.println("| 1 - ATIVO\n2 - INATIVO");
            int statusInput = InputUtils.lerIntOpcional(sc,"| Informe o numero do status (ENTER para manter): ");

            // Mesmo caso do cargo, mas faz a validacao para o status, para nao permitir numeros fora do range
            if (statusInput != 0) {
                if (statusInput >= 1 && statusInput <= 2) {
                    Status status = Status.values()[statusInput - 1];
                    funcionario.setStatus(status);
                } else {
                    System.out.println("| Status invalido. Mantendo status atual. |");
                }
            }

            // Edita o objeto funcionario utilizando o funcionarioService, que atualiza os dados do funcionario na lista
            funcionarioService.editar(funcionario);

            System.out.println("|--------------------------------------|");
            System.out.println("| Funcionario atualizado com sucesso!  |");
            System.out.println("|--------------------------------------|");
        }catch (Exception e) {
            System.out.println("Erro ao editar funcionarios: " + e.getMessage());
        }

    }
}