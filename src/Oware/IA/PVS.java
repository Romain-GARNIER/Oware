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

    @Override
    public String start(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){
        String bestCoup = "";
        double maxValue = -10000;
        Position pos_next;
        boolean first = true;
        double a = alpha;
        double b = beta;
        double score;
        ArrayList<String> coups = coupPossible(pos_current, player_one);
        for (String coup : coups){
            pos_next = gc.playMove(pos_current, player_one, coup);
            if (!first){
                score = -pvs(pos_next, first, player_one,  false, depth + 1, depthMax, -a -1, -a);
                if (a < score && score < b)
                    score = -pvs (pos_next, first, player_one, false, depth+1, depthMax, -b, -score);
                if(score == -10000)
                    bestCoup = coups.get(0);
            }else{
                score = -pvs(pos_next, first, player_one,false, depth + 1, depthMax, -b, -a);
                a = Math.max(a, score);
                if(score == -10000)
                    bestCoup = coups.get(0);
                if (a >= b) break;
            }
            if (maxValue < score){
                maxValue = score;
                bestCoup = coup;
            }
            first = false;
        }
        if(maxValue == -10000)
            bestCoup = coups.get(0);
        IHM.log("MinMax :"+maxValue,1);
        return bestCoup;
    }


    private double pvs(Position pos_current, boolean first, boolean player_one, boolean my_turn, int depth, int depthMax, double alpha, double beta){
        Position pos_next;
        double score;
        double a = alpha;
        double b = beta;

        if(GameControler.finalPosition(pos_current, my_turn)){
            int evaluation = GameControler.finalEvaluation(pos_current, player_one && my_turn);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
        }
        if (depth == depthMax){
            return gc.evaluation(posInit, pos_current, player_one);
        }
        ArrayList<String> coups = coupPossible(pos_current, player_one);
        for (String coup : coups){
            pos_next = gc.playMove(pos_current, player_one, coup);
            if (!first){
                score = -pvs(pos_next, first, player_one, !my_turn, depth + 1, depthMax, -a -1, -a);
                if (a < score && score < b)
                    score = -pvs (pos_next, first, player_one, !my_turn, depth+1, depthMax, -b, -score);
            }else{
                score = -pvs(pos_next, first, player_one, !my_turn, depth + 1, depthMax, -b, -a);
                a = Math.max(a, score);
                if (a >= b) break;
            }
            first = false;
        }
        return a;
    }
}
