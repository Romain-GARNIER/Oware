package Oware;

import Oware.IA.AlphaBetaCut;
import Oware.IA.IAEngine;
import Oware.IA.PVS;

public class ComputerPlayer extends Player{
    IAEngine iaEngine;
    int depth_start = 0;
    int depth_max = 7;
    int start = 0;
    int end = 96;

    public ComputerPlayer(IAEngine iaEngine, boolean player_one){
        this.iaEngine = iaEngine;
        this.player_one = player_one;
    }

    @Override
    public String chooseMove(Position position) {
        String move;

        move = iaEngine.start(position,player_one,depth_start,depth_max,start,end);
        //move = pvs.pvsStart(position,true,depth_start,depth_max,start,end);
        IHM.console("coup choisi par le bot : "+move);

        return move;
    }

    @Override
    public int chooseStartSpecialSeed(Position position) {
        int hole;

        hole = iaEngine.selectSpecialSeed(position, player_one, depth_start, depth_max, start, end);

        return hole;
    }
}
