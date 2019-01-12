package Oware.IA;

import Oware.GameControler;
import Oware.IHM;
import Oware.Position;


import java.util.ArrayList;
import java.util.Arrays;

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

public class PVS {
    private Position posInit;
    private GameControler gc;

    public PVS(Position posInit, GameControler gc) {
        this.posInit = posInit;
        this.gc = gc;
    }


    ArrayList<String> coupPossible(Position pos_current, boolean computer_play){
        ArrayList<String> coupsPossible = new ArrayList<>();
        ArrayList<String> cellComputer = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        ArrayList<String> cellPlayer = new ArrayList<>(Arrays.asList("7", "8", "9", "10", "11", "12"));
        ArrayList<String> couleur = new ArrayList<>(Arrays.asList("R", "B"));

        ArrayList<String> currentCell = (gc.computer_player_one) ? cellComputer : cellPlayer;

        for(String cellC : currentCell){
            for(String couleurC : couleur){
                String move = cellC + "-" + couleurC;
                int cellCS = (gc.computer_player_one) ? 0 : 6;
                if(GameControler.containsSpecialSeed(pos_current, computer_play, Integer.parseInt(cellC)-1-cellCS)){
                    for(int i=1; i<=3;i++){
                        move = cellC + "-" + couleurC + "-" + i;
                        if(GameControler.validMove(pos_current, computer_play, move)){
                            coupsPossible.add(move);
                        }
                    }

                }else{
                    if(GameControler.validMove(pos_current, computer_play, move)){
                        coupsPossible.add(move);
                    }
                }
            }
        }

        return coupsPossible;
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

        if(GameControler.finalPosition(pos_current, computer_play)){
            int evaluation = GameControler.finalEvaluation(pos_current);
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
