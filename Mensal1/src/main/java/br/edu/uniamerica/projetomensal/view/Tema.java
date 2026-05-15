package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.InputStream;

public class Tema {

    // ========== CORES ==========
    public static final Color COR_FUNDO          = new Color(18, 18, 18);   // Preto quase puro
    public static final Color COR_FUNDO_PAINEL   = new Color(28, 28, 28);   // Preto um pouco mais claro
    public static final Color COR_FUNDO_CAMPO    = new Color(40, 40, 40);   // Cinza escuro para campos
    public static final Color COR_DESTAQUE       = new Color(220, 20, 20);  // Vermelho principal
    public static final Color COR_DESTAQUE_HOVER = new Color(180, 10, 10);  // Vermelho escuro para hover
    public static final Color COR_TEXTO          = new Color(240, 240, 240); // Branco suave
    public static final Color COR_TEXTO_SECUNDARIO = new Color(160, 160, 160); // Cinza claro
    public static final Color COR_BORDA          = new Color(60, 60, 60);   // Borda sutil

    // ========== FONTES ==========
    public static Font FONTE_REGULAR;
    public static Font FONTE_BOLD;
    public static Font FONTE_TITULO;
    public static Font FONTE_BOTAO;
    public static Font FONTE_TABELA;

    // Carrega as fontes do arquivo .ttf
    static {
        try {
            InputStream streamRegular = Tema.class.getResourceAsStream("/fonts/Inter-Regular.ttf");
            InputStream streamBold    = Tema.class.getResourceAsStream("/fonts/Inter-Bold.ttf");

            Font interRegular = Font.createFont(Font.TRUETYPE_FONT, streamRegular);
            Font interBold    = Font.createFont(Font.TRUETYPE_FONT, streamBold);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(interRegular);
            ge.registerFont(interBold);

            FONTE_REGULAR = interRegular.deriveFont(13f);
            FONTE_BOLD    = interBold.deriveFont(Font.BOLD, 13f);
            FONTE_TITULO  = interBold.deriveFont(Font.BOLD, 18f);
            FONTE_BOTAO   = interBold.deriveFont(Font.BOLD, 12f);
            FONTE_TABELA  = interRegular.deriveFont(12f);

        } catch (Exception e) {
            // Fallback se nao encontrar o arquivo
            FONTE_REGULAR = new Font("SansSerif", Font.PLAIN, 13);
            FONTE_BOLD    = new Font("SansSerif", Font.BOLD, 13);
            FONTE_TITULO  = new Font("SansSerif", Font.BOLD, 18);
            FONTE_BOTAO   = new Font("SansSerif", Font.BOLD, 12);
            FONTE_TABELA  = new Font("SansSerif", Font.PLAIN, 12);
        }
    }

    // ========== METODOS AUXILIARES ==========

    // Aplica o tema global no UIManager — afeta todos os componentes Swing
    public static void aplicar() {
        UIManager.put("Panel.background",          COR_FUNDO);
        UIManager.put("OptionPane.background",     COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO);
        UIManager.put("Button.background",         COR_DESTAQUE);
        UIManager.put("Button.foreground",         COR_TEXTO);
        UIManager.put("Button.font",               FONTE_BOTAO);
        UIManager.put("Button.focusPainted",       false);
        UIManager.put("Label.foreground",          COR_TEXTO);
        UIManager.put("Label.font",                FONTE_REGULAR);
        UIManager.put("TextField.background",      COR_FUNDO_CAMPO);
        UIManager.put("TextField.foreground",      COR_TEXTO);
        UIManager.put("TextField.caretForeground", COR_TEXTO);
        UIManager.put("TextField.font",            FONTE_REGULAR);
        UIManager.put("TextField.border",          bordaCampo());
        UIManager.put("ComboBox.background",       COR_FUNDO_CAMPO);
        UIManager.put("ComboBox.foreground",       COR_TEXTO);
        UIManager.put("ComboBox.font",             FONTE_REGULAR);
        UIManager.put("ComboBox.selectionBackground", COR_DESTAQUE);
        UIManager.put("ComboBox.selectionForeground", COR_TEXTO);
        UIManager.put("Table.background",          COR_FUNDO_PAINEL);
        UIManager.put("Table.foreground",          COR_TEXTO);
        UIManager.put("Table.font",                FONTE_TABELA);
        UIManager.put("Table.selectionBackground", COR_DESTAQUE);
        UIManager.put("Table.selectionForeground", COR_TEXTO);
        UIManager.put("Table.gridColor",           COR_BORDA);
        UIManager.put("TableHeader.background",    COR_FUNDO);
        UIManager.put("TableHeader.foreground",    COR_DESTAQUE);
        UIManager.put("TableHeader.font",          FONTE_BOLD);
        UIManager.put("ScrollPane.background",     COR_FUNDO_PAINEL);
        UIManager.put("ScrollBar.background",      COR_FUNDO);
        UIManager.put("ScrollBar.thumb",           COR_BORDA);
        UIManager.put("TabbedPane.background",     COR_FUNDO);
        UIManager.put("TabbedPane.foreground",     COR_TEXTO);
        UIManager.put("TabbedPane.selected",       COR_FUNDO_PAINEL);
        UIManager.put("TabbedPane.font",           FONTE_BOLD);
        UIManager.put("SplitPane.background",      COR_FUNDO);
        UIManager.put("TitledBorder.titleColor",   COR_DESTAQUE);
        UIManager.put("TitledBorder.font",         FONTE_BOLD);
    }

    // Borda arredondada para campos de texto
    public static Border bordaCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }

    // Estiliza um botao com hover vermelho
    public static void estilizarBotao(JButton botao) {
        botao.setBackground(COR_DESTAQUE);
        botao.setForeground(COR_TEXTO);
        botao.setFont(FONTE_BOTAO);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botao.setBackground(COR_DESTAQUE_HOVER);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                botao.setBackground(COR_DESTAQUE);
            }
        });
    }
}