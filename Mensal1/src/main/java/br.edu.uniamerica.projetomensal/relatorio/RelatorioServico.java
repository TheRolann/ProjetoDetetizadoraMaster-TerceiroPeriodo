package br.edu.uniamerica.projetomensal.relatorio;

import br.edu.uniamerica.projetomensal.model.Servico;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import br.edu.uniamerica.projetomensal.service.ServicoService;
import br.edu.uniamerica.projetomensal.utils.InputUtils;
import java.time.format.DateTimeFormatter;

public class RelatorioServico {

    // Instanciamento do servicoService para acessar os serviços cadastrados
    private ServicoService servicoService = new ServicoService();

    // Relatorio geral de faturamento, sem ser por periodo, futuramente vai ser implementado
    public void totalFaturado() {
        // Atributos
        int quantidade = 0;
        int quantidadePendente = 0;
        double total = 0;
        double totalPendente = 0;
        double totalGeral = 0;
        double valorMaisLucrativo = 0;

        // Variavel para armazenar o nome do servico mais lucrativo
        String servicoMaisLucrativo = "";

        System.out.println("\n|--------------------------------------|");
        System.out.println("|               PENDENTES              |");
        System.out.println("|--------------------------------------|");

        // Loop para percorrer os servicos cadastrados e calcular os totais, alem de identificar o servico mais lucrativo
        for (Servico s : servicoService.listar()) {

            // Verificacao se for inativo, ignora o calculo do relatorio
            if(s.getStatus() == Status.INATIVO) {
                continue;
            }

            // Somatorio do valor total previsto, independente do status do servico
            totalGeral = totalGeral + s.getValor();

            // Somatorio e identificacao do servico mais lucrativo, considerando apenas os servicos concluidos
            if (s.getStatus() == Status.CONCLUIDO) {
                // Somatorio do valor total arrecadado, considerando apenas os servicos concluidos
                total += s.getValor();

                // Identificacao do servico mais lucrativo, considerando apenas os servicos concluidos
                if (s.getValor() > valorMaisLucrativo) {
                    valorMaisLucrativo = s.getValor();
                    servicoMaisLucrativo = s.getNomeServico();
                    // Quantidade 1 para o servico mais lucrativo, pois ele ja foi identificado como o mais lucrativo, entao ja tem 1 servico realizado
                    quantidade = 1;
                } else if (s.getNomeServico().equals(servicoMaisLucrativo)) {
                    // Caso o servico seja igual ao mais lucrativo, entao incrementa a quantidade, pois tem mais de um servico com o mesmo valor mais lucrativo
                    quantidade++;
                }

            // Somatorio do valor total pendente, considerando apenas os servicos em andamento ou agendados
            } else if (s.getStatus() == Status.EM_ANDAMENTO || s.getStatus() == Status.AGENDADO) {
                totalPendente += s.getValor();
                quantidadePendente++;
                // Mostra os servicos pendentes, com o id, nome do servico, data e id do cliente
                System.out.println("| " + s.getId() + " | " + s.getNomeServico() + " | " + s.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " | Cliente ID: " + s.getCliente());
            }
        }

        // Prints dos dados calculados e identificados, separado por secao dos concluidos e pendentes, alem do total geral previsto
        System.out.println("|--------------------------------------|");
        System.out.println("| Total pendente: R$ " + totalPendente);
        System.out.println("| Quantidade pendente: " + quantidadePendente);
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
}
