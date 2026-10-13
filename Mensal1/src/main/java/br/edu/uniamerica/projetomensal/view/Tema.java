package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.InputStream;

// Classe que centraliza o tema visual da aplicacao
// Aqui ficam cores, fontes e metodos auxiliares de estilo
public class Tema {

    // ========== CORES ==========
    // Cores principais usadas na interface escura
    public static final Color COR_FUNDO = new Color(22, 24, 29);               // Preto quase puro
    public static final Color COR_FUNDO_PAINEL = new Color(33, 36, 43);        // Preto um pouco mais claro
    public static final Color COR_FUNDO_CAMPO = new Color(43, 48, 57);         // Cinza escuro para campos
    public static final Color COR_DESTAQUE = new Color(224, 71, 76);           // Vermelho principal
    public static final Color COR_DESTAQUE_HOVER = new Color(200, 53, 60);     // Vermelho escuro para hover
    public static final Color COR_TEXTO = new Color(245, 246, 248);            // Branco suave
    public static final Color COR_TEXTO_SECUNDARIO = new Color(171, 179, 193); // Cinza claro
    public static final Color COR_BORDA = new Color(59, 66, 77);               // Borda sutil

    public static final Color COR_ERRO = new Color(255, 153, 153);
    public static final Color COR_SUCESSO = new Color(111, 212, 154);
    public static final int ESPACO = 8;
    public static final int ALTURA_CAMPO = 40;

    // ========== FONTES ==========
    // Fontes padrao carregadas do arquivo .ttf
    public static Font FONTE_REGULAR;
    public static Font FONTE_BOLD;
    public static Font FONTE_TITULO;
    public static Font FONTE_BOTAO;
    public static Font FONTE_TABELA;
    public static Font FONTE_MARCA;

    // Carrega as fontes do arquivo .ttf ao iniciar a classe
    // Se nao der certo, usa fontes padrao do sistema
    static {
        try {
            InputStream streamRegular = Tema.class.getResourceAsStream("/fonts/Inter-Regular.ttf");
            InputStream streamBold = Tema.class.getResourceAsStream("/fonts/Inter-Bold.ttf");

            Font interRegular = Font.createFont(Font.TRUETYPE_FONT, streamRegular);
            Font interBold = Font.createFont(Font.TRUETYPE_FONT, streamBold);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(interRegular);
            ge.registerFont(interBold);

            FONTE_REGULAR = interRegular.deriveFont(14f);
            FONTE_BOLD = interBold.deriveFont(Font.BOLD, 14f);
            FONTE_TITULO = interBold.deriveFont(Font.BOLD, 22f);
            FONTE_BOTAO = interBold.deriveFont(Font.BOLD, 14f);
            FONTE_TABELA = interRegular.deriveFont(13f);
            FONTE_MARCA = interBold.deriveFont(Font.BOLD, 26f);

        } catch (Exception e) {
            // Fallback caso as fontes nao sejam encontradas
            FONTE_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
            FONTE_BOLD = new Font("SansSerif", Font.BOLD, 14);
            FONTE_TITULO = new Font("SansSerif", Font.BOLD, 22);
            FONTE_BOTAO = new Font("SansSerif", Font.BOLD, 14);
            FONTE_TABELA = new Font("SansSerif", Font.PLAIN, 13);
            FONTE_MARCA = new Font("SansSerif", Font.BOLD, 26);
        }
    }

    // ========== METODOS AUXILIARES ==========

    // Aplica o tema global no UIManager
    // Isso afeta o visual padrao de varios componentes Swing
    // Swing passa a usar essas cores e fontes como padrao para varios componentes
    public static void aplicar() {
        UIManager.put("Panel.background", COR_FUNDO);
        UIManager.put("OptionPane.background", COR_FUNDO_PAINEL);
        UIManager.put("OptionPane.messageForeground", COR_TEXTO);
        UIManager.put("Button.background", COR_DESTAQUE);
        UIManager.put("Button.foreground", COR_TEXTO);
        UIManager.put("Button.font", FONTE_BOTAO);
        UIManager.put("Button.focusPainted", false);
        UIManager.put("Label.foreground", COR_TEXTO);
        UIManager.put("Label.font", FONTE_REGULAR);
        UIManager.put("TextField.background", COR_FUNDO_CAMPO);
        UIManager.put("TextField.foreground", COR_TEXTO);
        UIManager.put("TextField.caretForeground", COR_TEXTO);
        UIManager.put("TextField.font", FONTE_REGULAR);
        UIManager.put("TextField.border", bordaCampo());
        UIManager.put("ComboBox.background", COR_FUNDO_CAMPO);
        UIManager.put("ComboBox.foreground", COR_TEXTO);
        UIManager.put("ComboBox.font", FONTE_REGULAR);
        UIManager.put("ComboBox.selectionBackground", COR_DESTAQUE);
        UIManager.put("ComboBox.selectionForeground", COR_TEXTO);
        UIManager.put("Table.background", COR_FUNDO_PAINEL);
        UIManager.put("Table.foreground", COR_TEXTO);
        UIManager.put("Table.font", FONTE_TABELA);
        UIManager.put("Table.selectionBackground", COR_DESTAQUE);
        UIManager.put("Table.selectionForeground", COR_TEXTO);
        UIManager.put("Table.gridColor", COR_BORDA);
        UIManager.put("TableHeader.background", COR_FUNDO);
        UIManager.put("TableHeader.foreground", COR_DESTAQUE);
        UIManager.put("TableHeader.font", FONTE_BOLD);
        UIManager.put("ScrollPane.background", COR_FUNDO_PAINEL);
        UIManager.put("ScrollBar.background", COR_FUNDO);
        UIManager.put("ScrollBar.thumb", COR_BORDA);
        UIManager.put("ScrollBar.track", COR_FUNDO_PAINEL);
        UIManager.put("ScrollBar.thumbHighlight", COR_BORDA);
        UIManager.put("ScrollBar.thumbDarkShadow", COR_BORDA);
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(COR_BORDA));
        UIManager.put("TabbedPane.background", COR_FUNDO);
        UIManager.put("TabbedPane.foreground", COR_TEXTO);
        UIManager.put("TabbedPane.selected", COR_FUNDO_PAINEL);
        UIManager.put("TabbedPane.font", FONTE_BOLD);
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 18, 10, 18));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.highlight", COR_BORDA);
        UIManager.put("TabbedPane.light", COR_BORDA);
        UIManager.put("TabbedPane.shadow", COR_BORDA);
        UIManager.put("TabbedPane.darkShadow", COR_BORDA);
        UIManager.put("SplitPane.background", COR_FUNDO);
        UIManager.put("TitledBorder.titleColor", COR_DESTAQUE);
        UIManager.put("TitledBorder.font", FONTE_BOLD);
    }

    // Cria uma borda padrao para campos de texto
    public static Border bordaCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        );
    }

    public static void estilizarBotao(JButton botao) { estilizarBotao(botao, true); }
    public static void estilizarBotaoSecundario(JButton botao) { estilizarBotao(botao, false); }

    private static void estilizarBotao(JButton botao, boolean principal) {
        botao.putClientProperty("tema.principal", principal);
        botao.setUI(new BotaoUI()); botao.setOpaque(false); botao.setContentAreaFilled(false);
        botao.setIcon(Icones.acao(botao.getText())); botao.setIconTextGap(8);
        botao.setFont(FONTE_BOTAO); botao.setFocusPainted(true); botao.setBorderPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        atualizarBotao(botao, false);
        if (Boolean.TRUE.equals(botao.getClientProperty("tema.configurado"))) return;
        botao.putClientProperty("tema.configurado", true);
        botao.addPropertyChangeListener("enabled", e -> atualizarBotao(botao, false));
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { atualizarBotao(botao, true); }
            public void mouseExited(java.awt.event.MouseEvent e) { atualizarBotao(botao, false); }
        });
    }
    private static void atualizarBotao(JButton botao, boolean hover) {
        boolean principal = Boolean.TRUE.equals(botao.getClientProperty("tema.principal"));
        botao.setBackground(!botao.isEnabled() ? COR_FUNDO_PAINEL : principal ? (hover ? COR_DESTAQUE_HOVER : COR_DESTAQUE) : (hover ? COR_BORDA : COR_FUNDO_CAMPO));
        botao.setForeground(botao.isEnabled() ? COR_TEXTO : COR_TEXTO_SECUNDARIO);
    }
}
