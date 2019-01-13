package Oware.IA;

import Oware.ComputerPlayer;
import Oware.GameControler;
import Oware.IHM;
import Oware.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AlphaBetaCut extends IAEngine{

    public AlphaBetaCut(GameControler gc, Position posInit) {
        super(gc,posInit);
    }

    @Override
    public int selectSpecialSeed(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta){
        int bestCell = 0;
        Position pos_next;
        double maxValue = -10000;

        ArrayList<Integer> cellPossible = new ArrayList<>(Arrays.asList(0,1 ,2 ,3 ,4 ,5));
        ArrayList<String> coupPossible = coupPossible(pos_current, player_one);

        for (int cell : cellPossible){
            Position posFictif = pos_current.clone();
            if(player_one)
                posFictif.cells_player_1[cell].setSpecialSeed(1);
            else
                posFictif.cells_player_2[cell].setSpecialSeed(1);
            for (String coup : coupPossible) {
                pos_next = GameControler.playMove(posFictif, player_one, coup);
                double value = AlphaBetaCutValue(pos_next, player_one,false, depth + 1, depthMax, alpha, beta);
                //System.out.println(value);
                if (maxValue < value || (maxValue == value && new Random().nextInt() % 2 == 0)) {
                    maxValue = value;
                    bestCell = cell;
                }
            }
        }
        return bestCell+1;
    }

    @Override
    public String start(Position pos_current, boolean player_one, int depth, int depthMax, double alpha, double beta) {
        String bestCoup = "";
        Position pos_next;
        double maxValue = -10000;

        ArrayList<String> coupPossible = sortCoupPossible(coupPossible(pos_current, player_one), sortBestMove());

        for (String coup : coupPossible) {
            pos_next = GameControler.playMove(pos_current, player_one, coup);
            double value = AlphaBetaCutValue(pos_next, player_one,false, depth + 1, depthMax, alpha, beta);
            //System.out.println(value + " - " + maxValue + " - " + coup + " " + bestCoup);
            if (maxValue < value || (maxValue == value && new Random().nextInt() % 2 == 0)) {
                maxValue = value;
                bestCoup = coup;
            }
        }
        IHM.log("MinMax :"+maxValue,1);
        return bestCoup;
    }

    double AlphaBetaCutValue(Position pos_current, boolean player_one, boolean my_turn, int depth, int depthMax, double alpha, double beta){
        Position pos_next; // In C : created on the stack: = very fast

        if(GameControler.finalPosition(pos_current, player_one && my_turn)){
            int evaluation = GameControler.finalEvaluation(pos_current, player_one);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
        }
        if (depth == depthMax) {
            int evaluation = GameControler.evaluation(posInit,pos_current, player_one);
            IHM.log("evaluation : "+evaluation+"\n",3);
            return evaluation;
            // the simplest evealution fucntion is the difference of the taken seeds
        }

        ArrayList<String> coupPossible = sortCoupPossible(coupPossible(pos_current, player_one), sortBestMove());

        if (my_turn){
            for(String coup: coupPossible){
                pos_next = GameControler.playMove(pos_current, player_one, coup);
                double value = AlphaBetaCutValue(pos_next, player_one, !my_turn, depth+1, depthMax, alpha, beta);
                alpha = Math.max(alpha, value);
                if (alpha >= beta) return beta;
            }
            return alpha;
        }else{
            for(String coup: coupPossible){
                pos_next = GameControler.playMove(pos_current, player_one, coup);
                double value = AlphaBetaCutValue(pos_next, player_one, !my_turn, depth+1, depthMax, alpha, beta);
                beta = Math.min(beta, value);
                if(alpha>=beta) return alpha;
            }
            return beta;
        }
    }

    //
//    ArrayList<String> coupPossible(Position pos_current, boolean computer_play){
//        ArrayList<String> coupsPossible = new ArrayList<>();
//        ArrayList<String> cellComputer = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
//        ArrayList<String> cellPlayer = new ArrayList<>(Arrays.asList("7", "8", "9", "10", "11", "12"));
//        ArrayList<String> couleur = new ArrayList<>(Arrays.asList("R", "B"));
//
//        ArrayList<String> currentCell = (gc.computer_player_one) ? cellComputer : cellPlayer;
//
//        for(String cellC : currentCell){
//            for(String couleurC : couleur){
//                String move = cellC + "-" + couleurC;
//                int cellCS = (gc.computer_player_one) ? 0 : 6;
//                if(GameControler.containsSpecialSeed(pos_current, computer_play, Integer.parseInt(cellC)-1-cellCS)){
//                    for(int i=1; i<=3;i++){
//                        move = cellC + "-" + couleurC + "-" + i;
//                        if(GameControler.validMove(pos_current, computer_play, move)){
//                            coupsPossible.add(move);
//                        }
//                    }
//
//                }else{
//                    if(GameControler.validMove(pos_current, computer_play, move)){
//                        coupsPossible.add(move);
//                    }
//                }
//            }
//        }
//
//        return coupsPossible;
//    }
//
//    ArrayList<String> sortCoupPossible(ArrayList<String> coupPossible, ArrayList<String> bestMove){
//        ArrayList<String> res = new ArrayList<>();
//        ArrayList<String> copyCoupPossible = new ArrayList<String>(coupPossible);
//        ArrayList<String> firstCoup = new ArrayList<>();
//        ArrayList<String> secondCoup = new ArrayList<>();
//        for(String s : copyCoupPossible){
//            if (s.split("-")[0] == bestMove.get(0)){
//                firstCoup.add(s);
//            }
//        }
//        copyCoupPossible.removeAll(firstCoup);
//        res.addAll(firstCoup);
//        for(String s: copyCoupPossible){
//            if(s.split("-")[1] == bestMove.get(1)){
//                secondCoup.add(s);
//            }
//        }
//        copyCoupPossible.removeAll(secondCoup);
//        res.addAll(secondCoup);
//        res.addAll(copyCoupPossible);
//        return res;
//    }
//
//    ArrayList<String> sortBestMove(){
//        ArrayList<String> res = new ArrayList<>();
//        int black = posInit.seeds_player_1;
//        int red = posInit.seeds_player_1;
//
//        if (black >= red){
//            res.add("B");
//            res.add("R");
//        }else{
//            res.add("R");
//            res.add("B");
//        }
//        return res;
//    }


}
