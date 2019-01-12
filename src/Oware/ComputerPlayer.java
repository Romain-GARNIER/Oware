package Oware;

import Oware.IA.AlphaBetaCut;
import Oware.IA.IterativeDeepeningPSV;
import Oware.IA.PVS;

public class ComputerPlayer implements Player{
    AlphaBetaCut alphaBetaCut;
    PVS pvs;
    IterativeDeepeningPSV iterativeDeepeningPSV;
    int depth_start = 0;
    int depth_max = 9;
    int depth_max_SpeecialSeed = 7;
    int start = -96;
    int end = 96;
    int TIME_LIMIT_MILLIS = 2000;

    public ComputerPlayer(Position position, GameControler game){
        pvs = new PVS(position, game);
        alphaBetaCut = new AlphaBetaCut(game, position);
        iterativeDeepeningPSV = new IterativeDeepeningPSV(position, game, TIME_LIMIT_MILLIS);
    }

    @Override
    public String chooseMove(Position position) {
        String move;

        //move = alphaBetaCut.AlphaBetaCutStart(position,true,depth_start,depth_max,start,end);
        //move = pvs.PVSStart(position,true,depth_start,depth_max,start,end);
        move = iterativeDeepeningPSV.IterativeDeepeningPVSStart(position, true, start, end);

        IHM.console("coup choisi par le bot : "+move);

        return move;
    }

    @Override
    public int chooseStartSpecialSeed(Position position) {
        int hole;

        hole = alphaBetaCut.AlphBetaCutSeed(position, true, depth_start, depth_max_SpeecialSeed, start, end);

        return hole;
    }
}
