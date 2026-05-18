package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

// Classe utilitaria que centraliza o estilo visual da interface
// Ela aplica cores, fontes e bordas para deixar a tela padronizada
public class EstiloUtils {

    // Aplica fundo escuro em componentes visuais de forma recursiva
    public static void aplicarFundoEscuro(Component componente) {

        // Se for um JPanel, aplica cor de fundo e percorre os filhos
        if (componente instanceof JPanel panel) {
            panel.setBackground(Tema.COR_FUNDO);
            panel.setForeground(Tema.COR_TEXTO);
            for (Component filho : panel.getComponents()) {
                aplicarFundoEscuro(filho);
            }

        // Se for um JSplitPane, aplica fundo e trata os dois lados
        } else if (componente instanceof JSplitPane split) {
            split.setBackground(Tema.COR_FUNDO);
            aplicarFundoEscuro(split.getLeftComponent());
            aplicarFundoEscuro(split.getRightComponent());

        // Se for um JScrollPane, ajusta a cor da area interna e da view
        } else if (componente instanceof JScrollPane scroll) {
            scroll.setBackground(Tema.COR_FUNDO_PAINEL);
            scroll.getViewport().setBackground(Tema.COR_FUNDO_PAINEL);
            aplicarFundoEscuro(scroll.getViewport().getView());

        // Se for uma JTable, usa o estilo padrao da tabela
        } else if (componente instanceof JTable tabela) {
            estilizarTabela(tabela);

        // Se for um JLabel, ajusta cor e fonte
        } else if (componente instanceof JLabel label) {
            label.setForeground(Tema.COR_TEXTO);
            label.setFont(Tema.FONTE_REGULAR);

        // Se for um campo de texto, aplica o estilo de entrada
        } else if (componente instanceof JTextField campo) {
            estilizarCampo(campo);

        // Se for um combo box, aplica o estilo de selecao
        } else if (componente instanceof JComboBox<?> combo) {
            estilizarCombo(combo);

        // Se for um botao, reutiliza o estilo centralizado no Tema
        } else if (componente instanceof JButton botao) {
            Tema.estilizarBotao(botao);
        }
    }

    // Estiliza campos de texto com cores e borda padrao
    public static void estilizarCampo(JTextField campo) {
        campo.setBackground(Tema.COR_FUNDO_CAMPO);
        campo.setForeground(Tema.COR_TEXTO);
        campo.setCaretColor(Tema.COR_TEXTO);
        campo.setFont(Tema.FONTE_REGULAR);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),  // Borda de destaque
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    // Estiliza JComboBox e o render dos itens do dropdown
    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(Tema.COR_FUNDO_CAMPO);
        combo.setForeground(Tema.COR_TEXTO);
        combo.setFont(Tema.FONTE_REGULAR);
        combo.setBorder(BorderFactory.createLineBorder(Tema.COR_BORDA, 1));

        // Estiliza a lista suspensa do combo
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? Tema.COR_DESTAQUE : Tema.COR_FUNDO_CAMPO);
                setForeground(Tema.COR_TEXTO);
                setFont(Tema.FONTE_REGULAR);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }

    // Estiliza tabelas com o tema escuro da aplicacao
    public static void estilizarTabela(JTable tabela) {
        tabela.setBackground(Tema.COR_FUNDO_PAINEL);
        tabela.setForeground(Tema.COR_TEXTO);
        tabela.setFont(Tema.FONTE_TABELA);
        tabela.setRowHeight(28);
        tabela.setSelectionBackground(Tema.COR_DESTAQUE);
        tabela.setSelectionForeground(Tema.COR_TEXTO);
        tabela.setGridColor(Tema.COR_BORDA);
        tabela.setShowGrid(true);
        tabela.setIntercellSpacing(new Dimension(1, 1));

        // Configura o cabecalho da tabela
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(Tema.COR_FUNDO);
        header.setForeground(Tema.COR_DESTAQUE);
        header.setFont(Tema.FONTE_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.COR_DESTAQUE));
    }
}