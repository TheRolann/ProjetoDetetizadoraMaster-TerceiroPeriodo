package br.edu.uniamerica.projetomensal.utils;

// Classe para excecoes personalizadas para os servicos
public class NegocioException extends RuntimeException {
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
