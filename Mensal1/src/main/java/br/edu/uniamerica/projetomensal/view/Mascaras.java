package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.text.*;

public final class Mascaras {
    public enum Tipo { CPF, DOCUMENTO, TELEFONE }
    private Mascaras() {}

    public static JTextField campo(Tipo tipo) {
        JTextField campo = new JTextField();
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException { replace(fb, offset, 0, text, attrs); }
            @Override public void remove(FilterBypass fb, int offset, int length) throws BadLocationException { replace(fb, offset, length, "", null); }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String antes = fb.getDocument().getText(0, fb.getDocument().getLength());
                String entrada = text == null ? "" : text;
                if (!entrada.matches("[0-9\\s()./-]*")) { campo.putClientProperty("erroMascara", "Use somente números e pontuação do documento ou telefone."); return; }
                String numeros = digitos(antes.substring(0, offset) + entrada + antes.substring(offset + length));
                int posicao = digitos(antes.substring(0, offset) + entrada).length();
                // Backspace/Delete sobre pontuação também remove um dígito, evitando prender o cursor.
                if (entrada.isEmpty() && length > 0 && digitos(antes.substring(offset, offset + length)).isEmpty()) {
                    int indice = campo.getCaretPosition() > offset ? posicao - 1 : posicao;
                    if (indice >= 0 && indice < numeros.length()) { numeros = numeros.substring(0, indice) + numeros.substring(indice + 1); posicao = indice; }
                }
                int limite = tipo == Tipo.DOCUMENTO ? 14 : 11;
                if (numeros.length() > limite) { campo.putClientProperty("erroMascara", "Use até " + limite + " dígitos."); return; }
                campo.putClientProperty("erroMascara", null);
                String formatado = formatar(numeros, tipo);
                fb.replace(0, fb.getDocument().getLength(), formatado, attrs);
                int cursor = cursor(formatado, posicao);
                SwingUtilities.invokeLater(() -> { if (campo.getText().equals(formatado)) campo.setCaretPosition(cursor); });
            }
        });
        return campo;
    }

    public static String digitos(String texto) { return texto == null ? "" : texto.replaceAll("[^0-9]", ""); }
    public static String formatar(String texto, Tipo tipo) {
        String numeros = digitos(texto);
        String padrao = switch (tipo) {
            case CPF -> "###.###.###-##";
            case DOCUMENTO -> numeros.length() <= 11 ? "###.###.###-##" : "##.###.###/####-##";
            case TELEFONE -> numeros.length() <= 10 ? "(##) ####-####" : "(##) #####-####";
        };
        StringBuilder resultado = new StringBuilder(); int indice = 0;
        for (char caractere : padrao.toCharArray()) {
            if (indice >= numeros.length()) break;
            resultado.append(caractere == '#' ? numeros.charAt(indice++) : caractere);
        }
        return resultado.toString();
    }
    private static int cursor(String texto, int quantidade) {
        if (quantidade <= 0) return 0;
        int vistos = 0;
        for (int i = 0; i < texto.length(); i++) if (Character.isDigit(texto.charAt(i)) && ++vistos == quantidade) return i + 1;
        return texto.length();
    }
}
