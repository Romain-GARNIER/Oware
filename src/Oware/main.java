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

        System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,5));

        System.out.println(position.toString(gameControler.computer_player_one));

        while (!GameControler.finalPosition(position,false,0)){
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            int hole;
            System.out.println("Joueur 1 :");
//            hole = Integer.parseInt(sc.next());
            hole = minMax.minMaxValue(position,gameControler.computer_player_one,0,5);

            System.out.println(position.toString(gameControler.computer_player_one));

            position = GameControler.playMove(position,gameControler.computer_player_one,hole);
            System.out.println("coup choisi par le bot : "+hole);

//            System.out.println("MinMax : "+minMax.minMaxValue(position,!gameControler.computer_player_one,0,3));

            System.out.println(position.toString(gameControler.computer_player_one));

            if(!GameControler.finalPosition(position,false,0)){System.out.println("Joueur 2 :");
                System.out.println("---------------------------------------------------------------------------------------------------------------");
                hole = Integer.parseInt(sc.next());
                position = GameControler.playMove(position,!gameControler.computer_player_one,hole);

                //System.out.println("MinMax : "+minMax.minMaxValue(position,gameControler.computer_player_one,0,3));

                System.out.println(position.toString(gameControler.computer_player_one));
            }
        }
    }
}

