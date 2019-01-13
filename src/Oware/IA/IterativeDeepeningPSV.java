package Oware.IA;

import Oware.GameControler;
import Oware.IHM;
import Oware.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class IterativeDeepeningPSV extends IAEngine{
    private int TIME_LIMIT_MILLIS;
    private static boolean searchCutoff = false;
    private static final int limitDepthMax = 15;

    public IterativeDeepeningPSV(GameControler gc, Position posInit, int TIME_LIMIT_MILLIS) {
        super(gc,posInit);
        this.posInit = posInit;
        this.gc = gc;
        this.TIME_LIMIT_MILLIS = TIME_LIMIT_MILLIS;
    }

    @Override
    public int selectSpecialSeed(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){
        int bestCell = 0;
        double maxValue = -10000;
        ArrayList<Integer> cellPossible = new ArrayList<>(Arrays.asList(0,1 ,2 ,3 ,4 ,5));

        for (int cell : cellPossible){
            Position posFictif = pos_current.clone();
            if(player_one)
                posFictif.cells_player_1[cell].setSpecialSeed(1);
            else
                posFictif.cells_player_2[cell].setSpecialSeed(1);
            MoveWrapper<String, Double> wrapper = new MoveWrapper<>();
            wrapper = PVSIterative(pos_current, player_one, true, alpha, beta);
            if (maxValue < wrapper.score || (maxValue == wrapper.score && new Random().nextInt() % 2 == 0)) {
                maxValue = wrapper.score;
                bestCell = cell;
            }
        }
        if(maxValue == -10000)
            bestCell = 0;
        return bestCell+1;
    }

    @Override
    public String start(Position pos_current, boolean player_one, int depth, int depthMax,  double alpha, double beta){
        MoveWrapper<String, Double> BestMove = PVSIterative(pos_current, player_one, true, alpha, beta);
        IHM.log("MinMax :"+ BestMove.score,1);
        return BestMove.move;
    }


    public MoveWrapper<String, Double> PVSIterative(Position pos_current, boolean player_one, boolean my_turn, double alpha, double beta){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + TIME_LIMIT_MILLIS;
        int depth = 1;
        double max = -10000;
        Position pos_next;
        MoveWrapper<String, Double> BestMove = null;


        ArrayList<String> coups = coupPossible(pos_current, player_one);
        for(String coup : coups){
            long searchTimeLimit = ((TIME_LIMIT_MILLIS - 1000) / (coups.size()));
            pos_next = gc.playMove(pos_current, player_one, coup);
            double score = PVSIterativeSearch(pos_next, player_one, !my_turn, alpha, beta, searchTimeLimit);
            if (score > max){
                max = score;
                BestMove.move = coup;
                BestMove.score = score;
            }
        }

        return BestMove;
    }

    public double PVSIterativeSearch(Position pos_current, boolean player_one, boolean my_turn, double alpha, double beta, long timeLimit){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeLimit;
        int depthMax = 0;
        double score = 0;

        searchCutoff = false;

        while (depthMax <= limitDepthMax) {
            long currentTime = System.currentTimeMillis();

            if (currentTime >= endTime) {
                break;
            }

            double PVSresult = PVS(pos_current, player_one, my_turn, 1, depthMax, alpha, beta, currentTime,endTime - currentTime);

            if (!searchCutoff) {
                score = PVSresult;
            }

            depthMax++;
        }


        return score;
    }

    public double PVS(Position pos_current, boolean player_one, boolean my_turn, int depth, int depthMax,  double alpha, double beta,  long startTime, long timeLimit) {
        Position pos_next;
        double b = beta;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime);

        if (elapsedTime >= timeLimit) {
            searchCutoff = true;
        }

        if(GameControler.finalPosition(pos_current, player_one) && my_turn){
            int evaluation = GameControler.finalEvaluation(pos_current, player_one);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return -evaluation;
        }
        if (depth == depthMax){
            int eval = GameControler.evaluation(posInit, pos_current, player_one);
            //System.out.println("pvs eval : " + eval);
            return -eval;
        }
        if (searchCutoff){
            int eval = GameControler.evaluation(posInit, pos_current, player_one);
            return -eval;
        }

        ArrayList<String> coups = coupPossible(pos_current, player_one);


        double score;
        boolean first = true;
        for(String coup : coups){
            pos_next = gc.playMove(pos_current, player_one, coup);
            score = PVSScore(pos_next, player_one, !my_turn, first, depth+1, depthMax, alpha, beta, b, startTime, timeLimit);
            if (score > alpha){
                alpha = score;
                if (alpha >= beta){
                    break;
                }
            }
            b = alpha + 1;
            first = false;
        }

        return alpha;


    }

    public double PVSScore(Position pos_current, boolean player_one, boolean my_turn, final boolean first, final int depth, final int depthMax, final double alpha, final double beta, double b, long startTime, long timeLimit){
        double score = -PVS(pos_current, player_one, my_turn,depth, depthMax, -b, -alpha, startTime, timeLimit);
        if (!first && alpha < score && score < beta){
            score = -PVS(pos_current, player_one, my_turn,depth, depthMax, -beta, -alpha, startTime, timeLimit);
        }
        return score;
    }
}
