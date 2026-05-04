package br.edu.uniamerica.projetomensal;

import br.edu.uniamerica.projetomensal.config.PersistenceManager;
import br.edu.uniamerica.projetomensal.config.FlywayConfig;
import br.edu.uniamerica.projetomensal.menu.MenuPrincipal;

public class Main {
     public static void main (String[] args){

        FlywayConfig.migrar(); // Roda as migrations antes, atualizando com o banco de dados
        PersistenceManager.conectar(); // Abre a conexao JPA

        MenuPrincipal servicoMenu = new MenuPrincipal();

         // Inicia o menu principal
        servicoMenu.iniciar();

        PersistenceManager.desconectar(); // Fecha tudo
    }
}