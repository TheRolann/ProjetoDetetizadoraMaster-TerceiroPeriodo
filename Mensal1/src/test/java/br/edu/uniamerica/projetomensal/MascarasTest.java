package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.view.Mascaras;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MascarasTest {
    @Test void colaCpfCnpjETelefoneSemDuplicarPontuacao() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JTextField documento=Mascaras.campo(Mascaras.Tipo.DOCUMENTO);
            documento.setText("529.982.247-25"); assertEquals("529.982.247-25",documento.getText());
            documento.setText("11222333000181"); assertEquals("11.222.333/0001-81",documento.getText());
            JTextField telefone=Mascaras.campo(Mascaras.Tipo.TELEFONE);
            telefone.setText("(45) 99999-9999"); assertEquals("(45) 99999-9999",telefone.getText());
            telefone.setText("4533334444"); assertEquals("(45) 3333-4444",telefone.getText());
        });
    }
    @Test void editarNoMeioEApagarPontuacaoNaoPrendeCursor() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                JTextField campo=Mascaras.campo(Mascaras.Tipo.CPF); campo.setText("52998224725");
                campo.setCaretPosition(4); ((AbstractDocument)campo.getDocument()).remove(3,1);
                assertEquals("5298224725",Mascaras.digitos(campo.getText()));
                ((AbstractDocument)campo.getDocument()).replace(0,campo.getText().length(),"11144477735",null);
                assertEquals("111.444.777-35",campo.getText());
            } catch(Exception ex) { throw new RuntimeException(ex); }
        });
    }
    @Test void rejeitaExcessoELetrasSemPerderOValorAtual() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JTextField campo=Mascaras.campo(Mascaras.Tipo.CPF); campo.setText("52998224725");
            campo.setText("123456789012"); assertEquals("529.982.247-25",campo.getText());
            campo.setText("cpf-invalido"); assertEquals("529.982.247-25",campo.getText());
        });
    }
}
