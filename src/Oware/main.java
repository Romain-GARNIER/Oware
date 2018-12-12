package Oware;

import java.util.Scanner;

public class main{
    public static void main (String[] args){
        GameControler gameControler = new GameControler();

        IHM.level = 2;

        IHM.console("Joueur 1 (1) / Joueur 2 (2) ? :");
        gameControler.definePlayer();

        gameControler.startGame();

        IHM.console("Joueur 1 : "+gameControler.totalSeeds(1)+" graines");
        IHM.console("Joueur 2 : "+gameControler.totalSeeds(2)+" graines");
        IHM.console("Vainqueur : joueur "+gameControler.winner());

    }
}

