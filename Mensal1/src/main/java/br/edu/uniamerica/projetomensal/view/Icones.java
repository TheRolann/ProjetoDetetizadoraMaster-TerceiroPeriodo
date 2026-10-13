package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Locale;

/** Ícones vetoriais pequenos, sem arquivos externos ou fontes de símbolos. */
public final class Icones {
    public enum Tipo { MARCA, CLIENTES, FUNCIONARIOS, SERVICOS, AGENDA, RELATORIOS, NOVO, EDITAR, EXCLUIR, ATUALIZAR, SALVAR, FECHAR, SAIR, BUSCAR, CONTATO }
    private Icones() {}
    public static Image marcaJanela() {
        BufferedImage imagem=new BufferedImage(48,48,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=imagem.createGraphics(); criar(Tipo.MARCA,48).paintIcon(new JLabel(),g,0,0); g.dispose();
        return imagem;
    }
    public static Icon criar(Tipo tipo, int tamanho) {
        return new Icon() {
            public int getIconWidth() { return tamanho; }
            public int getIconHeight() { return tamanho; }
            public void paintIcon(Component c, Graphics original, int x, int y) {
                Graphics2D g=(Graphics2D)original.create();
                g.translate(x,y); g.scale(tamanho/24.0,tamanho/24.0);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(c.isEnabled()?c.getForeground():Tema.COR_TEXTO_SECUNDARIO);
                g.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                switch(tipo) {
                    case MARCA -> {
                        Path2D escudo=new Path2D.Double(); escudo.moveTo(12,1); escudo.lineTo(22,5); escudo.lineTo(21,14); escudo.quadTo(19,20,12,23); escudo.quadTo(5,20,3,14); escudo.lineTo(2,5); escudo.closePath();
                        g.setColor(Tema.COR_DESTAQUE); g.fill(escudo); g.setColor(Tema.COR_TEXTO);
                        g.setStroke(new BasicStroke(2.4f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                        linha(g,7,15,7,8); linha(g,7,8,12,13); linha(g,12,13,17,8); linha(g,17,8,17,15);
                    }
                    case CLIENTES, FUNCIONARIOS -> {
                        g.drawOval(8,3,8,8); g.drawArc(4,12,16,13,0,180);
                        if(tipo==Tipo.FUNCIONARIOS) { linha(g,12,14,10,18); linha(g,10,18,12,21); linha(g,12,21,14,18); linha(g,14,18,12,14); }
                    }
                    case SERVICOS -> { g.drawRoundRect(3,7,18,14,3,3); g.drawRoundRect(8,3,8,4,2,2); linha(g,3,12,21,12); linha(g,11,11,13,11); }
                    case AGENDA -> { g.drawRoundRect(3,5,18,16,3,3); linha(g,3,10,21,10); linha(g,8,3,8,7); linha(g,16,3,16,7); linha(g,8,15,10,15); linha(g,14,15,16,15); }
                    case RELATORIOS -> { linha(g,4,3,4,21); linha(g,4,21,21,21); g.drawRoundRect(8,13,3,5,1,1); g.drawRoundRect(14,7,3,11,1,1); }
                    case NOVO -> { linha(g,12,4,12,20); linha(g,4,12,20,12); }
                    case EDITAR -> { linha(g,5,16,16,5); linha(g,16,5,20,9); linha(g,20,9,9,20); linha(g,9,20,4,21); linha(g,4,21,5,16); }
                    case EXCLUIR -> { linha(g,4,6,20,6); linha(g,9,3,15,3); g.drawRoundRect(6,6,12,15,2,2); linha(g,10,10,10,17); linha(g,14,10,14,17); }
                    case ATUALIZAR -> { g.drawArc(4,4,16,16,35,290); linha(g,20,4,20,10); linha(g,20,10,14,10); }
                    case SALVAR -> { g.drawRoundRect(3,3,18,18,2,2); g.drawRect(7,3,9,6); g.drawRect(7,14,10,7); }
                    case FECHAR -> { linha(g,6,6,18,18); linha(g,18,6,6,18); }
                    case SAIR -> { linha(g,10,4,4,4); linha(g,4,4,4,20); linha(g,4,20,10,20); linha(g,9,12,21,12); linha(g,16,7,21,12); linha(g,21,12,16,17); }
                    case BUSCAR -> { g.drawOval(3,3,12,12); linha(g,14,14,21,21); }
                    case CONTATO -> { g.drawRoundRect(3,5,18,14,2,2); linha(g,3,6,12,13); linha(g,12,13,21,6); }
                }
                g.dispose();
            }
        };
    }
    private static void linha(Graphics2D g,int x,int y,int x2,int y2) { g.drawLine(x,y,x2,y2); }
    public static Tipo secao(String titulo) {
        return switch(titulo) { case "Clientes" -> Tipo.CLIENTES; case "Funcionários" -> Tipo.FUNCIONARIOS; case "Serviços" -> Tipo.SERVICOS; case "Agenda" -> Tipo.AGENDA; default -> Tipo.RELATORIOS; };
    }
    public static Icon acao(String texto) {
        String nome=texto.toLowerCase(Locale.ROOT);
        Tipo tipo=nome.startsWith("novo")?Tipo.NOVO:nome.startsWith("editar")?Tipo.EDITAR:
            nome.startsWith("excluir")?Tipo.EXCLUIR:nome.startsWith("atualizar")?Tipo.ATUALIZAR:
            nome.startsWith("salvar")||nome.startsWith("exportar")?Tipo.SALVAR:
            nome.startsWith("sair")||nome.startsWith("entrar")?Tipo.SAIR:
            nome.startsWith("cancelar")||nome.startsWith("limpar")?Tipo.FECHAR:null;
        return tipo==null?null:criar(tipo,16);
    }
}
