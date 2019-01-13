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
        ArrayList<Integer> cellPossible = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

        for (int cell : cellPossible){
            Position posFictif = pos_current.clone();
            if(cell < 6)
                posFictif.cells_player_1[cell].setSpecialSeed(1);
            else{
                int pos_cell = cell % 6;
                posFictif.cells_player_2[pos_cell].setSpecialSeed(1);
            }
            MoveWrapper<String, Double> wrapper = new MoveWrapper<>();
            pvs(pos_current, player_one, true, depth, depthMax, alpha, beta, wrapper);
            if (maxValue < wrapper.score || (maxValue == wrapper.score && new Random().nextInt() % 2 == 0)) {
                maxValue = wrapper.score;
                bestCell = cell;
            }
        }
        if(maxValue == -10000)
            bestCell = 0;
        return bestCell+1;
    }

    public String start(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){
        MoveWrapper<String, Double> wrapper = new MoveWrapper<>();
        pvs(pos_current, player_one, true, depth, depthMax, alpha, beta, wrapper);
        IHM.log("MinMax :"+wrapper.score,1);
        return wrapper.move;
    }

    public double PVSScore(Position pos_current, boolean player_one,boolean my_turn, final boolean first, final int depth, final int depthMax, final double alpha, final double beta, double b){
        double score = -pvs(pos_current, player_one, my_turn, depth, depthMax, -b, -alpha, null);
        if (!first && alpha < score && score < beta){
            score = -pvs(pos_current, player_one, my_turn, depth, depthMax, -beta, -alpha, null);
        }
        return score;
    }

    public double pvs(Position pos_current, boolean player_one, boolean my_turn, int depth, int depthMax, double alpha, double beta, MoveWrapper<String, Double> wrapper){
        Position pos_next;
        double b = beta;
        String bestMoveLocal = null;
        double bestScore = -10000;

        if(GameControler.finalPosition(pos_current, player_one) && my_turn){
            int evaluation = GameControler.finalEvaluation(pos_current, player_one);
            return -evaluation;
        }
        if (depth == depthMax){
            int eval = GameControler.evaluation(posInit, pos_current, player_one);
            return -eval;
        }

        ArrayList<String> coups = coupPossible(pos_current, player_one);

        double score;
        boolean first = true;
        for(String coup : coups){
            pos_next = GameControler.playMove(pos_current, player_one, coup);
            score = PVSScore(pos_next, player_one, !my_turn, first, depth+1, depthMax, alpha, beta,b);
            if (score > alpha){
                alpha = score;
                bestMoveLocal = coup;
                bestScore = score;
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
