package br.edu.uniamerica.projetomensal.utils;

public class ValidacaoDocumentos {

    // Classe utilitaria para validar documentos brasileiros
    // Suporta CPF (11 digitos) e CNPJ (14 digitos)
    // Todos os comentarios estao em formato simples para facilitar o estudo

    // Valida CPF ou CNPJ automaticamente baseado no tamanho
    public static boolean validar(String documento) {
        String limpo = documento.replaceAll("[./-]",""). trim();

        // Remove pontuacao e verifica o tamanho
        if (limpo.length() == 11) return validarCPF(limpo); // CPF
        if (limpo.length() == 14) return validarCNPJ(limpo); // CNPJ
        return false; // se nao for 11 nem 14 digitos, eh invalido
    }

    // ======== CPF ==========
    public static boolean validarCPF(String cpf) {

        // Rejeita casos triviais onde todos os digitos sao iguais
        if (cpf.matches("(\\d)\\1{10}")) return false;

        // Calcula o primeiro digito verificador segundo a regra do CPF
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }

        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) primeiroDigito = 0;

        // Verifica o primeiro digito calculado com o digito na posicao 9
        if (primeiroDigito != Character.getNumericValue(cpf.charAt(9))) return false;

        // Calcula o segundo digito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }

        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) segundoDigito = 0;

        // Compara com o digito na posicao 10
        return segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    // ======== CNPJ ==========
    public static boolean validarCNPJ(String cnpj) {
        // Rejeita casos triviais onde todos os digitos sao iguais
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        // Primeiro digito verificador usa pesos especificos
        int[] pesosPrimeiro = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesosPrimeiro[i];
        }
        int primeiroDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        // Verifica o digito calculado com o digito na posicao 12
        if (primeiroDigito != Character.getNumericValue(cnpj.charAt(12))) return false;

        // Segundo digito verificador, com outro conjunto de pesos
        int[] pesosSegundo = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesosSegundo[i];
        }
        int segundoDigito = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        // Compara com o digito na posicao 13
        return segundoDigito == Character.getNumericValue(cnpj.charAt(13));
    }
}
