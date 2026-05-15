package br.edu.uniamerica.projetomensal.utils;

import java.time.LocalDate;
import java.util.Scanner;

// Classe para ler e validar entradas de usuario, garantindo que os dados sejam do tipo esperado e evitando erros de formato
// Sempre lendo em String e convertendo para o tipo desejado com tratamento, limpando a entrada e validando o formato antes de converter
// para evitar NumberFormatException e garantir que o usuario digite um valor valido

// Metodos de leitura e que retorna a mensagem para o terminal tbm, sendo util eficiente
public class InputUtils {
    // Metodo para ler um numero inteiro, validando a entrada e tratando erros de formato
    public static int lerInt(Scanner sc, String mensagem) {
        // Loop para continuar solicitando a entrada ate que o usuario digite um numero inteiro valido
        while (true) {
                System.out.print(mensagem);
                String entrada = sc.nextLine().trim(); // Remove espacos em branco no inicio e no final da entrada para evitar erros de formato
                if (entrada.isEmpty()) { // Verifica se a entrada esta vazia, se sim, solicita novamente
                    System.out.println("Entrada vazia. Por favor, digite um numero inteiro.");
                    continue;
                }
            try { // Tenta converter a entrada para inteiro, se nao, lanca um NumberFormatException
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um numero inteiro.");
            }
        }
    }

    // Metodo para ler um numero decimal, validando a entrada e tratando erros de formato
    public static double lerDouble(Scanner sc, String mensagem) {
        // Loop para continuar solicitando a entrada ate que o usuario digite um numero decimal valido
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim(); // Remove espacos em branco no inicio e no final da entrada para evitar erros de formato
            if (entrada.isEmpty()) { // Verifica se a entrada esta vazia, se sim, solicita novamente
                System.out.println("Entrada vazia. Por favor, digite um numero valido.");
                continue;
            }
            try { // Tenta converter a entrada para double, se nao, lanca um NumberFormatException.
                // Substitui virgula por ponto para permitir formatos decimais
                return Double.parseDouble(entrada.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um numero valido.");
            }
        }
    }

    // Metodo para ler uma String, e evitar string vazia ou espacos em branco
    public static String lerString(Scanner sc, String mensagem) {
        // Loop para continuar solicitando a entrada ate que o usuario digite um texto valido
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) { // Verifica se a entrada esta vazia, se sim, solicita novamente
                System.out.println("Entrada vazia. Por favor, digite um texto valido.");
                continue;
            }
            try {
                return entrada; // Caso a entrada seja valida, se nao, lanca erro, mas nesse caso acho dificil, mas deixa ai
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um texto valido.");
            }
        }
    }

    // Metodo para ler apenas numeros, validando a entrada e garantindo que o usuario digite apenas caracteres numericos, sem letras ou simbolos
    public static String lerSomenteNumeros(Scanner sc, String mensagem) {
        // Loop padrao
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) { // Verificacao se a entrada esta vazia, se sim, solicita novamente
                System.out.println("Entrada vazia. Por favor, digite um valor valido.");
                continue;
            }
            // Aqui, a expressao regular "\\d+" verifica se a entrada consiste apenas de caracteres numericos (0-9) e tem pelo menos um caracter, garantindo que o usuario digite apenas numeros
            if (!entrada.matches("\\d+")) { // Expressao regular regex. "\d" = 1 - 9, "+" = um ou mais caracteres, \d+ = um ou mais numeros
                System.out.println("Entrada invalida. Por favor, digite apenas numeros.");
                continue;
            }
            return entrada;
        }
    }

    // Metodo para validacao de documentos como cpf e cnpj sem o digito verificador
    public static String lerDocumento(Scanner sc, String mensagem) {
        // Loop
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) { // Verificacao de entrada vazia, se sim, solicita novamente
                System.out.println("Entrada vazia. Por favor, digite um valor valido.");
                continue;
            }

            // Criando uma String para limpar a entrada, removendo os caracteres de formatacao
            // replaceAll, no regex o que queremos substituir, e colocamos nada no lugar ""
            String documentoLimpo = entrada.replaceAll("[./-]", ""); // Remove pontos, barras e hifens da entrada

            // Verificacao com matches, a expressao regular "\\d{11}|\\d{14}" verifica se a entrada limpa tem exatamente 11 ou 14 numeros
            if (!documentoLimpo.matches("\\d{11}|\\d{14}")) { // "\\" significa que queremos um numero literal, "|" ou
                System.out.println("Entrada invalida. O documento deve conter exatamente 11 ou 14 numeros.");
                continue;
            }

            // Retorna o documento limpo e formatado, sem nada alem dos numeros
            return documentoLimpo;
        }
    }

    // Classe para ler datas, validando o formato e garantindo que o usuario digite uma data valida no formato DD/MM/AAAA
    public static String lerData(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Entrada vazia. Por favor, digite uma data valida.");
                continue;
            }
            // Valida se tem o formato DD/MM/AAAA com /
            if (!entrada.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Entrada invalida. A data deve estar no formato DD/MM/AAAA.");
                continue;
            }

            // Extrai dia, mes e ano da entrada
            String[] partes = entrada.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            // Valida se a data realmente existente
            try {
                LocalDate.of(ano, mes, dia);
                return entrada; // Se conseguir criar a data, ela é válida
            } catch (Exception e) {
                System.out.println("Data inexistente. Por favor, digite uma data valida (ex: 25/12/2026).");
                continue;
            }
        }
    }

    // Metodo para ler numeros de telefone, validando o formato e garantindo que o usuario digite um numero de telefone valido, com 10 ou 11 digitos, sem letras ou simbolos
    public static String lerTelefone(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Entrada vazia. Por favor, digite um numero de telefone valido.");
                continue;
            }
            // A expressao regular "\\d{10,11}" verifica se a entrada consiste apenas de numeros e tem entre 10 e 11 digitos, permitindo formatos de telefone com ou sem o nono digito
            if (!entrada.matches("\\d{10,11}")) { // Verifica se a entrada tem entre 10 e 11 numeros
                System.out.println("Entrada invalida. O numero de telefone deve conter entre 10 e 11 numeros.");
                continue;
            }
            return entrada;
        }
    }

    // Metodo para ler email, com utilizacao do regex para validar o formato do email
    public static String lerEmail(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Entrada vazia. Por favor, digite um email valido.");
                continue;
            }
            // A expressao regular "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$" verifica se a entrada tem um formato de email valido, com caracteres antes e depois do @ e um dominio
            if (!entrada.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { // Verifica se a entrada tem um formato de email valido
                System.out.println("Entrada invalida. Por favor, digite um email valido.");
                continue;
            }
            return entrada;
        }
    }

    // Metodo para formatar a data, como no sistema a data e salvo sequencial, DDMMYYYY, esse metodo formata com /
    public static String formatarData(String data) {
        if (data == null || data.length() != 10) { // Verificacao se e nula ou tamanho diferente
            throw new IllegalArgumentException("Data deve conter exatamente 8 caracteres no formato DDMMYYYY.");
        }
        // substring faz a separacao da string, pegando os caracteres correspondentes a dia, mes e ano
        String dia = data.substring(0, 2);
        String mes = data.substring(2, 4);
        String ano = data.substring(4, 8);

        return dia + "/" + mes + "/" + ano; // Retorna a data formatada no formato DD/MM/YYYY
    }

    // Opcionais para permitir campo vazio, retornando string vazia ou 0 para numeros
    public static String lerStringOpcional(Scanner sc, String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return sc.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um texto valido.");
            }
        }
    }

    public static int lerIntOpcional(Scanner sc, String mensagem) {
        while(true) {
            try {
                System.out.print(mensagem);
                String entrada = sc.nextLine().trim();
                if (entrada.isEmpty()) {
                    return 0; // Retorna 0 se o usuario nao digitar nada
                }
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um numero inteiro.");
            }
        }
    }

    public static double lerDoubleOpcional(Scanner sc, String mensagem) {
        while(true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(sc.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um numero valido.");
            }
        }
    }

    // Versao para permitir campo vazio
    public static String lerDataOpcional(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                return ""; // Retorna string vazia se o usuario nao digitar nada
            }
            // Valida se tem o formato DD/MM/AAAA com /
            if (!entrada.matches("\\d{2}/\\d{2}/\\d{4}")) {
                System.out.println("Entrada invalida. A data deve estar no formato DD/MM/AAAA.");
                continue;
            }

            // Extrai dia, mes e ano da entrada
            String[] partes = entrada.split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);

            // Valida se a data é realmente existente
            try {
                LocalDate.of(ano, mes, dia);
                return entrada; // Se conseguir criar a data, ela é válida
            } catch (Exception e) {
                System.out.println("Data inexistente. Por favor, digite uma data valida (ex: 25/12/2026).");
                continue;
            }
        }
    }

    // Versao para permitir campo vazio
    public static String lerTelefoneOpcional(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                return ""; // Retorna string vazia se o usuario nao digitar nada
            }
            if (!entrada.matches("\\d{10,11}")) {
                System.out.println("Entrada invalida. O numero de telefone deve conter entre 10 e 11 numeros.");
                continue;
            }
            return entrada;
        }
    }

    // Versao para permitir campo vazio
    public static String lerEmailOpcional(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                return ""; // Retorna string vazia se o usuario nao digitar nada
            }
            if (!entrada.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                System.out.println("Entrada invalida. Por favor, digite um email valido.");
                continue;
            }
            return entrada;
        }
    }

    // Metodo para ler valor de servico, validando que seja maior que 0
    public static double lerValorServico(Scanner sc, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("Entrada vazia. Por favor, digite um valor valido.");
                continue;
            }
            try {
                double valor = Double.parseDouble(entrada.replace(",", "."));
                if (valor <= 0) {
                    System.out.println("Entrada invalida. O valor do servico deve ser maior que 0.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, digite um numero valido.");
            }
        }
    }
}