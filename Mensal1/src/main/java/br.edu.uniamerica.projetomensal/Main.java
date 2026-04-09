package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.config.FlywayConfig;
import br.edu.uniamerica.projetomensal.menu.MenuPrincipal;

public class Main {
     public static void main (String[] args){
        // Inicia o menu principal
        FlywayConfig.migrar(); // Roda migrations antes
        PersistenceManager.conectar(); // Conecta ao BD

        MenuPrincipal servicoMenu = new MenuPrincipal();
        servicoMenu.iniciar();

        PersistenceManager.desconectar(); // Fecha o BD
    }
}