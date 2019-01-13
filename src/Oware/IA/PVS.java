package Oware.IA;

import Oware.GameControler;
import Oware.IHM;
import Oware.Position;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

   /*
   function pvs(node, depth, α, β, color)
     *     if node is a terminal node or depth = 0
     *         return color x the heuristic value of node
     *     for each child of node
     *         if child is not first child
     *             score := -pvs(child, depth-1, -α-1, -α, -color)       (* search with a null window *)
     *             if α < score < β                                      (* if it failed high,
     *                 score := -pvs(child, depth-1, -β, -score, -color)         do a full re-search *)
     *         else
     *             score := -pvs(child, depth-1, -β, -α, -color)
     *         α := max(α, score)
     *         if α >= β
     *             break                                            (* beta cut-off *)
     *     return α
     * </pre>
    */


public class PVS extends IAEngine{

    public PVS(GameControler gc, Position posInit) {
        super(gc,posInit);
    }


    @Override
    public int selectSpecialSeed(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){
        int bestCell = 0;
        Position pos_next;
        double maxValue = -10000;
        boolean first = true;
        ArrayList<Integer> cellPossible = new ArrayList<>(Arrays.asList(0,1 ,2 ,3 ,4 ,5));
        ArrayList<String> coupPossible = coupPossible(pos_current, player_one);

        for (int cell : cellPossible){
            Position posFictif = pos_current.clone();
            if(player_one)
                posFictif.cells_player_1[cell].setSpecialSeed(1);
            else
                posFictif.cells_player_2[cell].setSpecialSeed(1);
            for (String coup : coupPossible) {
                pos_next = GameControler.playMove(posFictif, true, coup);
                double value = pvs(pos_next,first,player_one, false, depth + 1, depthMax, alpha, beta);
                //System.out.println(value);
                if (maxValue < value || (maxValue == value && new Random().nextInt() % 2 == 0)) {
                    maxValue = value;
                    bestCell = cell;
                }
            }
        }
        if(maxValue == -10000)
            bestCell = 0;
        return bestCell+1;
    }

    public String PVSStart(Position pos_current, boolean computer_play, int depth, int depthMax, double alpha, double beta){
        MoveWrapper<String, Double> wrapper = new MoveWrapper<>();
        PVS(pos_current, computer_play, depth, depthMax, alpha, beta, wrapper);
        IHM.log("MinMax :"+wrapper.score,1);
        return wrapper.move;
    }

    public double PVSScore(Position pos_current, boolean computer_play, final boolean first, final int depth, final int depthMax, final double alpha, final double beta, double b){
        double score = -PVS(pos_current, computer_play, depth + 1, depthMax, -b, -alpha, null);
        if (!first && alpha < score && score < beta){
            score = -PVS(pos_current, computer_play, depth + 1, depthMax, -beta, -alpha, null);
        }
        return score;
    }

    public double PVS(Position pos_current, boolean computer_play, int depth, int depthMax, double alpha, double beta, MoveWrapper<String, Double> wrapper){
        Position pos_next;
        double b = beta;
        String bestMoveLocal = null;
        double bestScore = -10000;

        if(GameControler.finalPosition(pos_current, my_turn)){
            int evaluation = GameControler.finalEvaluation(pos_current, player_one && my_turn);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return -evaluation;
        }
        if (depth == depthMax){
            int eval = GameControler.evaluation(posInit, pos_current);
            //System.out.println("PVS eval : " + eval);
            return -eval;
        }

        ArrayList<String> coups = coupPossible(pos_current, computer_play);

        double score;
        boolean first = true;
        for(String coup : coups){
            pos_next = gc.playMove(pos_current, computer_play, coup);
            score = PVSScore(pos_next, !computer_play, first, depth, depthMax, alpha, beta,b);
            if (score > alpha){
                alpha = score;
                bestMoveLocal = coup;
                bestScore =  score;
                if (alpha >= beta){
                    break;
                }
            }
            b = alpha + 1;
            first = false;
        }
        if (wrapper != null) {
            wrapper.move = bestMoveLocal;
            wrapper.score = bestScore;
        }
        return alpha;


    }

}
