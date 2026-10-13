package br.edu.uniamerica.projetomensal.utils;

// Identifica o campo sem acoplar regras de negócio aos componentes Swing.
public class ErroCampo extends NegocioException {
    private final String campo;
    public ErroCampo(String campo, String mensagem) { super(mensagem); this.campo = campo; }
    public String campo() { return campo; }
}
