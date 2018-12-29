package Oware;

import java.util.Scanner;

public class HumanPlayer implements Player{

    @Override
    public String chooseMove(Position position) {

        IHM.console("Choisissez un trou entre 1 et 6 :");
        String move = IHM.consoleNext();
        if(!GameControler.validMove(position,false,move)){
            while (!GameControler.validMove(position,false,move)){
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
