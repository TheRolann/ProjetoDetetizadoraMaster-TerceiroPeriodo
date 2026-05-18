package br.edu.uniamerica.projetomensal.utils;

// Classe para excecoes personalizadas para regras de negocio
// Usada pelos services para sinalizar violacoes de regra
// Extende RuntimeException para ser uma excecao nao checada
public class NegocioException extends RuntimeException {
    // Construtor que recebe a mensagem que sera exibida quando a excecao for lancada
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}