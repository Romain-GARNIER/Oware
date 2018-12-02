package Oware;

import java.util.Scanner;

public class main{
    public static void main (String[] args){
        GameControler gameControler = new GameControler();
        Scanner sc = new Scanner(System.in);

        System.out.println("Joueur 1 (1) / Joueur 2 (2) ? :");
        int player = Integer.parseInt(sc.next());
        gameControler.definePlayer(player);

        Position position = new Position();
        position.init();

        System.out.println(position.toString(gameControler.computer_player_one));

        while (!GameControler.finalPosition(position,false,0)){
            int hole;
            System.out.println("Joueur 1 :");
            hole = Integer.parseInt(sc.next());
            GameControler.playMove(position,position,gameControler.computer_player_one,hole);
            System.out.println(position.toString(gameControler.computer_player_one));

            System.out.println("Joueur 2 :");
            hole = Integer.parseInt(sc.next());
            GameControler.playMove(position,position,!gameControler.computer_player_one,hole);
        }
    }
}
