package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class RelatorioServico {

    // Instanciamento do servicoService para acessar os serviços cadastrados
    private final ServicoService servicoService = new ServicoService();
    private final Scanner sc = new Scanner(System.in);

    // Formatador de data no padrao do sistema
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Relatorio de servico com escolha entre geral ou por periodo
    public void totalFaturado() {

        List<Servico> servicos = servicoService.listar();

        // Validacao para lista vazia
        if (servicos == null || servicos.isEmpty()) {
            System.out.println("\n|--------------------------------------|");
            System.out.println("| Nenhum servico cadastrado.           |");
            System.out.println("|--------------------------------------|");
            return;
        }

        while (true) {
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
                    gerarRelatorio(servicos, null, null, false);
                    return;

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

                    // Protecao extra contra null, mesmo com a validacao anterior
                    if (inicio == null || fim == null) {
                        System.out.println("|--------------------------------------|");
                        System.out.println("| Erro ao converter as datas.          |");
                        System.out.println("|--------------------------------------|");
                        return;
                    }

                    if (inicio.isAfter(fim)) {
                        System.out.println("|--------------------------------------|");
                        System.out.println("| Periodo invalido.                    |");
                        System.out.println("| A data inicial nao pode ser maior.   |");
                        System.out.println("|--------------------------------------|");
                        continue;
                    }

                    gerarRelatorio(servicos, dataInicial, dataFinal, true);
                    return;

                case 0:
                    System.out.println("Voltando ao menu de relatorios...");
                    return;

                default:
                    System.out.println("|--------------------------------------|");
                    System.out.println("| Opcao invalida.                      |");
                    System.out.println("| Escolha 0, 1 ou 2.                   |");
                    System.out.println("|--------------------------------------|");
            }
        }
    }

    // Metodo interno para gerar tanto o relatorio geral quanto o relatorio por periodo
    private void gerarRelatorio(List<Servico> servicos, String dataInicial, String dataFinal, boolean filtrarPorPeriodo) {

        int quantidadeMaisLucrativo = 0;
        int quantidadePendente = 0;
        int pendenteHoje = 0;
        int pendenteFuturo = 0;
        int pendenteAtrasado = 0;
        int ignoradosPorErro = 0;
        int totalServicosNoRelatorio = 0;

        double total = 0;
        double totalPendente = 0;
        double totalGeral = 0;
        double valorMaisLucrativo = 0;

        String servicoMaisLucrativo = "Nenhum";

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

        for (Servico s : servicos) {

            // Validacao para item nulo na lista
            if (s == null) {
                ignoradosPorErro++;
                continue;
            }

            // Validacao para status nulo
            if (s.getStatus() == null) {
                ignoradosPorErro++;
                continue;
            }

            // Verificacao se for inativo, ignora o calculo do relatorio
            if (s.getStatus() == Status.INATIVO) {
                continue;
            }

            LocalDate dataServico = s.getData();

            // Filtro por periodo, quando selecionado
            if (filtrarPorPeriodo) {
                // Se a data do servico for invalida, nao entra no relatorio por periodo
                if (dataServico == null || dataServico.isBefore(inicio) || dataServico.isAfter(fim)) {
                    continue;
                }
            }

            String nomeServico = validarNomeServico(s.getNomeServico());
            double valorServico = validarValorServico(s.getValor());

            // Se o valor for invalido, ignora do somatorio mas ainda pode mostrar informacao basica
            if (valorServico < 0) {
                ignoradosPorErro++;
                continue;
            }

            totalServicosNoRelatorio++;
            totalGeral += valorServico;

            if (s.getStatus() == Status.CONCLUIDO) {
                total += valorServico;

                if (valorServico > valorMaisLucrativo) {
                    valorMaisLucrativo = valorServico;
                    servicoMaisLucrativo = nomeServico;
                    quantidadeMaisLucrativo = 1;
                } else if (valorServico == valorMaisLucrativo && nomeServico.equals(servicoMaisLucrativo)) {
                    quantidadeMaisLucrativo++;
                }

            } else if (s.getStatus() == Status.EM_ANDAMENTO || s.getStatus() == Status.AGENDADO) {
                totalPendente += valorServico;
                quantidadePendente++;

                // Calcula a situacao da agenda (hoje, futuro, atrasado)
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

                // Mostra os servicos pendentes, com o id, nome do servico, data, id do cliente e situacao
                System.out.println("| " + s.getId()
                        + " | " + nomeServico
                        + " | " + (dataServico != null ? dataServico.format(formatter) : "SEM DATA")
                        + " | Cliente ID: " + s.getCliente().getId()
                        + " | " + situacaoAgenda);
            }
        }

        if (totalServicosNoRelatorio == 0) {
            System.out.println("| Nenhum servico encontrado para esse relatorio. |");
            System.out.println("|--------------------------------------|");
            return;
        }

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
        System.out.println("| Quantidade realizada: " + quantidadeMaisLucrativo);
        System.out.println("|--------------------------------------|");

        System.out.println("\n|--------------------------------------|");
        System.out.println("|              TOTAL GERAL             |");
        System.out.println("|--------------------------------------|");
        System.out.println("| Faturamento total previsto: R$ " + totalGeral);
        System.out.println("| Servicos considerados: " + totalServicosNoRelatorio);
        System.out.println("| Registros ignorados por erro: " + ignoradosPorErro);
        System.out.println("|--------------------------------------|");
    }

    // Metodo auxiliar para ler data valida ou retornar ao menu com 0
    private String lerDataValidaOuVoltar(String mensagem) {
        while (true) {
            String entrada = InputUtils.lerString(sc, mensagem).trim();

            if (entrada.equals("0")) {
                return null;
            }

            // Validacao de formato
            if (!entrada.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Data invalida. Digite no formato DD/MM/AAAA ou 0 para voltar.");
                continue;
            }

            // Validacao de data real
            LocalDate data = converterData(entrada);
            if (data == null) {
                System.out.println("Data inexistente. Digite uma data valida ou 0 para voltar.");
                continue;
            }

            return entrada;
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

    // Metodo auxiliar para validar nome do servico
    private String validarNomeServico(String nomeServico) {
        if (nomeServico == null || nomeServico.trim().isEmpty()) {
            return "Servico sem nome";
        }
        return nomeServico.trim();
    }

    // Metodo auxiliar para validar valor do servico
    private double validarValorServico(double valorServico) {
        if (valorServico < 0) {
            return -1;
        }
        return valorServico;
    }

    // Metodo auxiliar para exibir a data sem quebrar o relatorio
    private String validarDataExibicao(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "SEM DATA";
        }
        return data;
    }
}