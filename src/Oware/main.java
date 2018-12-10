package Oware;

import java.util.Scanner;

public class main{
    public static void main (String[] args){
        GameControler gameControler = new GameControler();

        IHM.level = 1;

        IHM.console("Joueur 1 (1) / Joueur 2 (2) ? :");
        gameControler.definePlayer();

        gameControler.startGame();


    }
}

