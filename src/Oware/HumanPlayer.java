package Oware;

import java.util.Scanner;

public class HumanPlayer extends Player{

    public HumanPlayer(boolean player_one){
        this.player_one = player_one;
    }

    @Override
    public String chooseMove(Position position) {

        IHM.console("Choisissez un trou entre 1 et 6 :");
        String move = IHM.consoleNext();
        if(!GameControler.validMove(position,player_one,move)){
            while (!GameControler.validMove(position,player_one,move)){
                IHM.console("Le coup n'est pas valide : ");
                IHM.console("Choisissez un trou entre 1 et 6 :");
                move = IHM.consoleNext();
            }
        }

        return move;
    }

    @Override
    public int chooseStartSpecialSeed(Position position) {
        int hole;

        hole = IHM.consoleNextInt();

        return hole;
    }


}
