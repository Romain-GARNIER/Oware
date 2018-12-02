package Oware;

import java.util.Scanner;

public class main{
    public static void main (String[] args){
        GameControler gameControler = new GameControler();
        Scanner sc = new Scanner(System.in);
        MinMax minMax = new MinMax();

        System.out.println("Joueur 1 (1) / Joueur 2 (2) ? :");
//        int player = Integer.parseInt(sc.next());
        gameControler.definePlayer(2);

        Position position = new Position();
        position.init();

        System.out.println(position.toString(gameControler.computer_player_one));

        System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,5));

        while (!GameControler.finalPosition(position,false,0)){
            int hole;
            System.out.println("Joueur 1 :");
            hole = Integer.parseInt(sc.next());
            position = GameControler.playMove(position,gameControler.computer_player_one,hole);
            System.out.println(position.toString(gameControler.computer_player_one));

            System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,3));

            if(!GameControler.finalPosition(position,false,0)){System.out.println("Joueur 2 :");
                hole = Integer.parseInt(sc.next());
                position = GameControler.playMove(position,!gameControler.computer_player_one,hole);
                System.out.println(position.toString(gameControler.computer_player_one));

                System.out.println("MinMax : "+minMax.minMaxValue(position,!gameControler.computer_player_one,0,3));
            }
        }
    }
}
