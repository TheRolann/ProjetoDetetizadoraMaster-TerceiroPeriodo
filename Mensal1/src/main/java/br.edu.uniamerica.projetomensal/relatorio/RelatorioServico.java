package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class RelatorioServico {

    // Instanciamento do servicoService para acessar os serviços cadastrados
    private final ServicoService servicoService = new ServicoService();
    private final Scanner sc = new Scanner(System.in);

    // Formatador de data no padrao do sistema
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Relatorio de servico com escolha entre geral ou por periodo
    public void totalFaturado() {
        System.out.println("\n|--------------------------------------|");
        System.out.println("|     --- Relatorio de Servico ---     |");
        System.out.println("|--------------------------------------|");
        System.out.println("| 1 - Relatorio geral                  |");
        System.out.println("| 2 - Relatorio por periodo            |");
        System.out.println("| 0 - Voltar                           |");
        System.out.println("|--------------------------------------|");

        int opcao = InputUtils.lerInt(sc, "Opcao: ");

        switch (opcao) {
            case 1:
                gerarRelatorio(null, null, false);
                break;

            case 2:
                String dataInicial = lerDataValidaOuVoltar("| Data inicial (DD/MM/AAAA) ou 0 para voltar: ");
                if (dataInicial == null) {
                    System.out.println("Voltando ao menu de relatorios...");
                    return;
                }

                String dataFinal = lerDataValidaOuVoltar("| Data final (DD/MM/AAAA) ou 0 para voltar: ");
                if (dataFinal == null) {
                    System.out.println("Voltando ao menu de relatorios...");
                    return;
                }

                LocalDate inicio = converterData(dataInicial);
                LocalDate fim = converterData(dataFinal);

                if (inicio == null || fim == null) {
                    System.out.println("|--------------------------------------|");
                    System.out.println("| Erro inesperado ao validar datas.    |");
                    System.out.println("|--------------------------------------|");
                    return;
                }

                if (inicio.isAfter(fim)) {
                    System.out.println("|--------------------------------------|");
                    System.out.println("| Periodo invalido.                    |");
                    System.out.println("| A data inicial nao pode ser maior.   |");
                    System.out.println("|--------------------------------------|");
                    return;
                }

                gerarRelatorio(dataInicial, dataFinal, true);
                break;

            case 0:
                System.out.println("Voltando ao menu de relatorios...");
                return;

            default:
                System.out.println("Opcao invalida.");
        }
    }

    // Metodo interno para gerar tanto o relatorio geral quanto o relatorio por periodo
    private void gerarRelatorio(String dataInicial, String dataFinal, boolean filtrarPorPeriodo) {
        // Atributos
        int quantidade = 0;
        int quantidadePendente = 0;

        int pendenteHoje = 0;
        int pendenteFuturo = 0;
        int pendenteAtrasado = 0;

        double total = 0;
        double totalPendente = 0;
        double totalGeral = 0;
        double valorMaisLucrativo = 0;

        // Variavel para armazenar o nome do servico mais lucrativo
        String servicoMaisLucrativo = "";

        LocalDate inicio = null;
        LocalDate fim = null;

        if (filtrarPorPeriodo) {
            inicio = converterData(dataInicial);
            fim = converterData(dataFinal);
        }

        System.out.println("\n|--------------------------------------|");
        System.out.println("|               PENDENTES              |");
        System.out.println("|--------------------------------------|");

        if (filtrarPorPeriodo) {
            System.out.println("| Periodo: " + dataInicial + " ate " + dataFinal);
            System.out.println("|--------------------------------------|");
        }

        // Loop para percorrer os servicos cadastrados e calcular os totais, alem de identificar o servico mais lucrativo
        for (Servico s : servicoService.listar()) {

            // Verificacao se for inativo, ignora o calculo do relatorio
            if (s.getStatus() == Status.INATIVO) {
                continue;
            }

            LocalDate dataServico = converterData(s.getData());

            // Filtro por periodo, quando selecionado
            if (filtrarPorPeriodo) {
                if (dataServico == null || dataServico.isBefore(inicio) || dataServico.isAfter(fim)) {
                    continue;
                }
            }

            // Somatorio do valor total previsto, independente do status do servico
            totalGeral += s.getValor();

            // Somatorio e identificacao do servico mais lucrativo, considerando apenas os servicos concluidos
            if (s.getStatus() == Status.CONCLUIDO) {
                // Somatorio do valor total arrecadado, considerando apenas os servicos concluidos
                total += s.getValor();

                // Identificacao do servico mais lucrativo, considerando apenas os servicos concluidos
                if (s.getValor() > valorMaisLucrativo) {
                    valorMaisLucrativo = s.getValor();
                    servicoMaisLucrativo = s.getNomeServico();
                    quantidade = 1;
                } else if (s.getValor() == valorMaisLucrativo &&
                        s.getNomeServico().equals(servicoMaisLucrativo)) {
                    quantidade++;
                }

                // Somatorio do valor total pendente, considerando apenas os servicos em andamento ou agendados
            } else if (s.getStatus() == Status.EM_ANDAMENTO || s.getStatus() == Status.AGENDADO) {
                totalPendente += s.getValor();
                quantidadePendente++;

                LocalDate hoje = LocalDate.now();
                String situacaoAgenda = "SEM DATA";

                if (dataServico != null) {
                    if (dataServico.isEqual(hoje)) {
                        situacaoAgenda = "HOJE";
                        pendenteHoje++;
                    } else if (dataServico.isAfter(hoje)) {
                        situacaoAgenda = "FUTURO";
                        pendenteFuturo++;
                    } else {
                        situacaoAgenda = "ATRASADO";
                        pendenteAtrasado++;
                    }
                }

                // Mostra os servicos pendentes, com o id, nome do servico, data, id do cliente e situacao da agenda
                System.out.println("| " + s.getId()
                        + " | " + s.getNomeServico()
                        + " | " + s.getData()
                        + " | Cliente ID: " + s.getClienteID()
                        + " | " + situacaoAgenda);
            }
        }

        // Prints dos dados calculados e identificados
        System.out.println("|--------------------------------------|");
        System.out.println("| Total pendente: R$ " + totalPendente);
        System.out.println("| Quantidade pendente: " + quantidadePendente);
        System.out.println("|--------------------------------------|");

        System.out.println("|--------------------------------------|");
        System.out.println("|           RESUMO DA AGENDA           |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Hoje: " + pendenteHoje);
        System.out.println("| Futuro: " + pendenteFuturo);
        System.out.println("| Atrasados: " + pendenteAtrasado);
        System.out.println("|--------------------------------------|");

        System.out.println("\n|--------------------------------------|");
        System.out.println("| - --- Relatorio de Faturamento --- - |");
        System.out.println("|              CONCLUIDOS              |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Total arrecadado: R$ " + total);
        System.out.println("| Servico mais lucrativo: " + servicoMaisLucrativo);
        System.out.println("| Quantidade realizada: " + quantidade);
        System.out.println("|--------------------------------------|");

        System.out.println("\n|--------------------------------------|");
        System.out.println("|              TOTAL GERAL             |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Faturamento total previsto: R$ " + totalGeral);
        System.out.println("|--------------------------------------|");
    }

    // Metodo auxiliar para ler data valida ou retornar ao menu com 0
    private String lerDataValidaOuVoltar(String mensagem) {
        while (true) {
            String entrada = InputUtils.lerString(sc, mensagem).trim();

            if (entrada.equals("0")) {
                return null;
            }

            if (entrada.matches("\\d{2}/\\d{2}/\\d{4}")) {
                LocalDate data = converterData(entrada);
                if (data != null) {
                    return entrada;
                }
            }

            System.out.println("Data invalida. Digite no formato DD/MM/AAAA ou 0 para voltar.");
        }
    }

    // Metodo auxiliar para converter a data de String para LocalDate
    private LocalDate converterData(String data) {
        try {
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}