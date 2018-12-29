package Oware;

public class ComputerPlayer implements Player{
    AlphaBetaCut alphaBetaCut;
    int depth_start = 0;
    int depth_max = 7;
    int start = 0;
    int end = 96;

    @Override
    public String chooseMove(Position position) {
        String move;

        move = alphaBetaCut.AlphaBetaCutStart(position,true,depth_start,depth_max,start,end);
        IHM.console("coup choisi par le bot : "+move);

        return move;
    }

    @Override
    public int chooseStartSpecialSeed(Position position) {
        int hole;

        hole = alphaBetaCut.AlphBetaCutSeed(position, true, depth_start, depth_max, start, end);

        return hole;
    }
}
