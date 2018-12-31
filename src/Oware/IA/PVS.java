package Oware.IA;

import Oware.GameControler;
import Oware.Position;

import java.sql.Array;
import java.util.ArrayList;

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
    private Position posInit;
    private GameControler gc;

    public PVS(Position posInit, GameControler gc) {
        this.posInit = posInit;
        this.gc = gc;
    }

    public String pvsStart(Position pos_current, boolean computer_play, int depth, int depthMax, double alpha, double beta){
        String bestCoup = "";
        double maxValue = -10000;
        Position pos_next;
        boolean first = true;
        double a = alpha;
        double b = beta;
        double score;
        ArrayList<String> coups = coupPossible(pos_current, computer_play);
        for (String coup : coups){
            pos_next = gc.playMove(pos_current, computer_play, coup);
            if (!first){
                score = -pvs(pos_next, first,  !computer_play, depth + 1, depthMax, -a -1, -a);
                if (a < score && score < b) score = -pvs (pos_next, first, !computer_play, depth+1, depthMax, -b, -score);
            }else{
                score = -pvs(pos_next, first, !computer_play, depth + 1, depthMax, -b, -a);
                a = Math.max(a, score);
                if (a >= b) break;
            }
            if (maxValue < score){
                maxValue = score;
                bestCoup = coup;
            }
            first = false;
        }
        return bestCoup;
    }


    private double pvs(Position pos_current, boolean first, boolean computer_play, int depth, int depthMax, double alpha, double beta){
        Position pos_next;
        double score;
        double a = alpha;
        double b = beta;
        if (depth == depthMax || gc.finalPosition(pos_current)){
            return gc.evaluation(posInit, pos_current);
        }
        ArrayList<String> coups = coupPossible(pos_current, computer_play);
        for (String coup : coups){
            pos_next = gc.playMove(pos_current, computer_play, coup);
            if (!first){
                score = -pvs(pos_next, first,  !computer_play, depth + 1, depthMax, -a -1, -a);
                if (a < score && score < b) score = -pvs (pos_next, first, !computer_play, depth+1, depthMax, -b, -score);
            }else{
                score = -pvs(pos_next, first, !computer_play, depth + 1, depthMax, -b, -a);
                a = Math.max(a, score);
                if (a >= b) break;
            }
            first = false;
        }
        return a;
    }
}
