package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class EstiloUtils {

    // Aplica fundo escuro em todos os paineis recursivamente
    public static void aplicarFundoEscuro(Component componente) {
        if (componente instanceof JPanel panel) {
            panel.setBackground(Tema.COR_FUNDO);
            panel.setForeground(Tema.COR_TEXTO);
            for (Component filho : panel.getComponents()) {
                aplicarFundoEscuro(filho);
            }
        } else if (componente instanceof JSplitPane split) {
            split.setBackground(Tema.COR_FUNDO);
            aplicarFundoEscuro(split.getLeftComponent());
            aplicarFundoEscuro(split.getRightComponent());
        } else if (componente instanceof JScrollPane scroll) {
            scroll.setBackground(Tema.COR_FUNDO_PAINEL);
            scroll.getViewport().setBackground(Tema.COR_FUNDO_PAINEL);
            aplicarFundoEscuro(scroll.getViewport().getView());
        } else if (componente instanceof JTable tabela) {
            estilizarTabela(tabela);
        } else if (componente instanceof JLabel label) {
            label.setForeground(Tema.COR_TEXTO);
            label.setFont(Tema.FONTE_REGULAR);
        } else if (componente instanceof JTextField campo) {
            estilizarCampo(campo);
        } else if (componente instanceof JComboBox<?> combo) {
            estilizarCombo(combo);
        } else if (componente instanceof JButton botao) {
            Tema.estilizarBotao(botao);
        }
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setBackground(Tema.COR_FUNDO_CAMPO);
        campo.setForeground(Tema.COR_TEXTO);
        campo.setCaretColor(Tema.COR_TEXTO);
        campo.setFont(Tema.FONTE_REGULAR);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.COR_DESTAQUE, 1),  // Borda vermelha
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(Tema.COR_FUNDO_CAMPO);
        combo.setForeground(Tema.COR_TEXTO);
        combo.setFont(Tema.FONTE_REGULAR);
        combo.setBorder(BorderFactory.createLineBorder(Tema.COR_BORDA, 1));

        // Estiliza o dropdown
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

        // Cabecalho
        JTableHeader header = tabela.getTableHeader();
        header.setBackground(Tema.COR_FUNDO);
        header.setForeground(Tema.COR_DESTAQUE);
        header.setFont(Tema.FONTE_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Tema.COR_DESTAQUE));
    }
}