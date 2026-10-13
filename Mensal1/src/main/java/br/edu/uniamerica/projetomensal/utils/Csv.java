package br.edu.uniamerica.projetomensal.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public final class Csv {
    private Csv() {}
    public static void salvar(Path arquivo, String[] cabecalho, List<Object[]> linhas) throws IOException {
        try (var writer = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            writer.write('\uFEFF');
            writer.write(linha(cabecalho)); writer.newLine();
            for (Object[] valores : linhas) { writer.write(linha(valores)); writer.newLine(); }
        }
    }
    static String linha(Object[] valores) {
        return java.util.Arrays.stream(valores).map(Csv::celula).collect(java.util.stream.Collectors.joining(";"));
    }
    private static String celula(Object valor) {
        String texto = valor == null ? "" : valor.toString();
        // Evita interpretar dados de cadastro como fórmulas em planilhas.
        String inicio = texto.stripLeading();
        if (!inicio.isEmpty() && "=+@-".indexOf(inicio.charAt(0)) >= 0) texto = "'" + texto;
        return "\"" + texto.replace("\"", "\"\"") + "\"";
    }
}
