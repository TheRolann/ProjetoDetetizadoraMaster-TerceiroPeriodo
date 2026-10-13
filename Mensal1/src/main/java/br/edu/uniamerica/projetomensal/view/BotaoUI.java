package br.edu.uniamerica.projetomensal.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.*;

/** Desenha o fundo arredondado, mantendo texto, ícone e comportamento do Swing. */
public final class BotaoUI extends BasicButtonUI {
    protected void paintText(Graphics g,JComponent c,Rectangle area,String texto) {
        AbstractButton b=(AbstractButton)c;
        g.setColor(b.getForeground());
        BasicGraphicsUtils.drawStringUnderlineCharAt(g,texto,b.getDisplayedMnemonicIndex(),area.x,area.y+g.getFontMetrics().getAscent());
    }
    public void paint(Graphics original,JComponent c) {
        AbstractButton b=(AbstractButton)c; Graphics2D g=(Graphics2D)original.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(b.getModel().isPressed()&&b.isEnabled()?b.getBackground().darker():b.getBackground());
        g.fillRoundRect(0,0,c.getWidth(),c.getHeight(),12,12);
        if(b.isFocusPainted()&&b.hasFocus()) { g.setColor(Tema.COR_TEXTO); g.drawRoundRect(3,3,c.getWidth()-7,c.getHeight()-7,8,8); }
        g.dispose(); super.paint(original,c);
    }
}
