package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/** Abas com seleção clara e navegação por teclado preservada. */
public final class NavegacaoUI extends BasicTabbedPaneUI {
    protected void installDefaults() {
        super.installDefaults();
        tabInsets=new Insets(14,20,14,20); selectedTabPadInsets=new Insets(0,0,0,0);
        tabAreaInsets=new Insets(8,16,8,16); contentBorderInsets=new Insets(0,0,0,0);
        tabPane.setOpaque(true); tabPane.setBackground(Tema.COR_FUNDO);
        tabPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    protected void paintTabBackground(Graphics original,int placement,int index,int x,int y,int w,int h,boolean selected) {
        Graphics2D g=(Graphics2D)original.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(selected?Tema.COR_DESTAQUE:Tema.COR_FUNDO_PAINEL);
        g.fillRoundRect(x+3,y+2,w-6,h-4,12,12); g.dispose();
    }
    protected void paintTabBorder(Graphics g,int p,int i,int x,int y,int w,int h,boolean s) {}
    protected void paintContentBorder(Graphics g,int placement,int selected) {}
    protected void paintFocusIndicator(Graphics original,int placement,Rectangle[] rects,int index,Rectangle icon,Rectangle text,boolean selected) {
        if(!tabPane.hasFocus()||!selected) return;
        Rectangle r=rects[index]; Graphics2D g=(Graphics2D)original.create();
        g.setColor(Tema.COR_TEXTO); g.drawRoundRect(r.x+6,r.y+5,r.width-13,r.height-11,8,8); g.dispose();
    }
}
